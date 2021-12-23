import Amplify, { Auth, API, Storage } from 'aws-amplify';
import { AuthState, onAuthUIStateChange } from '@aws-amplify/ui-components';
import { AmplifyAuthenticator, AmplifySignOut } from '@aws-amplify/ui-react';
import axios from 'axios';
import awsconfig from '../aws-exports';
import {updateUser} from "../model/Model";
import {setPanelVisibility} from "../model/ViewModel";
import store from "../model/ModelProxy";
import { useSelector, useDispatch } from 'react-redux'
export default class Authcontroller {

    constructor() {

        onAuthUIStateChange((nextAuthState, authData) => {

            console.log(authData)
            console.log(nextAuthState)

            if (authData && nextAuthState == "signedin") {

                console.log(authData.signInUserSession.idToken.jwtToken)

                var authToken = authData.signInUserSession.idToken.jwtToken

                var groups = authData.signInUserSession.idToken.payload["cognito:groups"] ?? []
                var userId = authData.attributes.sub

                store.dispatch(updateUser({
                    userId: authData.username, 
                    username: authData.username, 
                    groups: groups, 
                    token: authToken 
                }));
            }
        });
    }

    logout() {
        console.log("logout!!")
        Auth.signOut()
        store.dispatch(setPanelVisibility({
            name: "auth_form", 
            state: false
        }));
        store.dispatch(updateUser(null));
    }

    login() {
        console.log("login!!")
        store.dispatch(setPanelVisibility({
            name: "auth_form", 
            state: true
        }));
    }

}