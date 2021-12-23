import axios from 'axios';
import awsconfig from '../aws-exports';
import { Config } from "../boundary/common/Config";
import http from 'http';
import https from 'https';
import store from '../model/ModelProxy';
import { enqueueNotification } from "../model/ViewModel";

export default class RequestService {

    constructor() {

        this.addRequestSuccessListeners = []
        this.removeRequestSuccessListeners = []
        
        this.getRequestListeners = []

        this.axiosClient = axios.create({
            timeout: 60000,
            httpAgent: new http.Agent({ keepAlive: true }),
            httpsAgent: new https.Agent({ keepAlive: true }),
            maxRedirects: 10
        });
          
    }

    registerAddRequestSuccessListener(cb) {

        this.addRequestSuccessListeners.push(cb)
    }

    executePostRequest(cb, data, endpoint, errMsg, successMsg, execListeners = true) {

        console.log("Executing post request: ", data)

        var model = store.getState().model;

        data.authorId = (model.currentUser) ? model.currentUser.username : null;

        this.axiosClient.post(Config.API_PATH + endpoint,
            data,
            {
                headers: {
                    'Authorization': (model.currentUser) ? (model.currentUser.token) : ""
                }
            }
        ).then(res => {

            console.log("PostRequest: Req Success: ", res)

            if (res.data.statusCode == "400" || res.data.statusCode == 400) {

                if(errMsg.length > 0) {

                    store.dispatch(enqueueNotification({
                        name: "add_request_status",
                        widgetKey: "",
                        status: "request_complete",
                        msg: errMsg + " Details: " + res.data.error ?? "None",
                        type: "error"
                    }))
                }


                cb(res.data.error)

            } else {

                if(successMsg.length > 0) {

                    store.dispatch(enqueueNotification({
                        name: "add_request_status",
                        widgetKey: "",
                        status: "request_complete",
                        msg: successMsg,
                        type: "success"
                    }))
                }

                if(execListeners) {

                    for (var i in this.addRequestSuccessListeners) {

                        this.addRequestSuccessListeners[i](res)
                    }
                }

                cb("",res.data)
            }

        }).catch((err) => {

            console.log("PostRequest: Req Failure: ", err)

            if(errMsg.length > 0) {

                store.dispatch(enqueueNotification({
                    name: "add_request_status",
                    widgetKey: "",
                    status: "request_complete",
                    msg: errMsg,
                    type: "error"
                }))
            }

            cb(err)
        })

    }

    executeGetRequest(cb, url, customUrl = false) {

        var model = store.getState().model;
        var finalUrl = url

        if(!customUrl) {
            finalUrl = Config.API_PATH + finalUrl
        }   

        this.axiosClient.get( finalUrl, {
            headers: {
                'Authorization': (model.currentUser) ? (model.currentUser.token) : ""
            }
        })
            .then(res => {
                console.log("GetRequest: ", res)

                if (res.data && res.data.statusCode !== 400 || res.data.statusCode !== "400") {

                    cb("", res.data)

                } else {

                    cb(res.data.error ?? "error", null)
                }
            }).catch(res => {
                console.log(res)
                cb("error", null)
            })
    }
}