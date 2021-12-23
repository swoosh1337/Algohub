import './App.css';

import React, { useState } from 'react';
import MainPage from './boundary/pages/MainPage'
import AccountManagementPage from './boundary/pages/AccountManagementPage';
import SignInPage from './boundary/pages/SignInPage';
import Amplify, { Auth, API, Storage } from 'aws-amplify';
import { AuthState, onAuthUIStateChange } from '@aws-amplify/ui-components';
import { AmplifyAuthenticator, AmplifySignOut } from '@aws-amplify/ui-react';
import axios from 'axios';
import awsconfig from './aws-exports';
import { Config } from "./boundary/common/Config"
import { useSnackbar } from 'notistack';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import OntologyController from './controllers/OntologyController';
import RequestService from './services/RequestService';
import AuthController from "./controllers/AuthController";
import store from './model/ModelProxy';
import { updateNotificationQueue, updateSelectedOntologyItem,updateRemoveRequest } from "./model/ViewModel";
import { useSelector, useDispatch } from 'react-redux';
import RemoveDialog from "./boundary/forms/RemoveDialog"

Amplify.configure(awsconfig);

function App() {

  const { enqueueSnackbar, closeSnackbar } = useSnackbar();
  const dispatch = useDispatch();

  var requestService = new RequestService();
  var authController = new AuthController();
  var ontologyController = new OntologyController(requestService);

  var authFormOpen = useSelector((state) => state.viewModel.openPanels.includes("auth_form"));
  var notificationQueue = useSelector((state) => state.viewModel.notificationQueue);
  var removeRequest = useSelector(state => state.viewModel.removeRequest)

  requestService.registerAddRequestSuccessListener((res) => {

    ontologyController.expandItem(res.data.id);
  });

  React.useEffect(() => {

    var temp = JSON.parse(JSON.stringify(notificationQueue));
    var notificationsToRemove = [];

    for (var i = 0; i < temp.length; i++) {

      var notification = temp[i];

      if (notification.status == "request_complete") {

        enqueueSnackbar(notification.msg,
          {
            anchorOrigin: {
              vertical: 'bottom',
              horizontal: 'right',
            },
            variant: notification.type
          });

          notificationsToRemove.push(i);
      }

      if(notification.status == "loading_started" && notification.widgetKey == "") {

        var key = enqueueSnackbar(notification.msg,
          {
            anchorOrigin: {
              vertical: 'bottom',
              horizontal: 'right',
            },
            variant: notification.type,
            persist: true
          });

        notification.widgetKey = key;


        dispatch(updateNotificationQueue(temp))

      }

      if (notification.status == "loading_complete") {
        console.log("LOADING COMPLETE!!!");
        closeSnackbar(notification.widgetKey);
        notificationsToRemove.push(i);
      }

    }

    if(notificationsToRemove.length > 0) {

      for(var i = 0; i < notificationsToRemove.length; i++) {

        temp.splice(notificationsToRemove[i], 1)
      }

      dispatch(updateNotificationQueue(temp))
    }

  }, [notificationQueue]);

  React.useEffect(() => {

    ontologyController.updateOntology(() => {
      var initiallySelected = ontologyController.getInitiallySelected()

      if(initiallySelected) {
        ontologyController.selectOntologyItem(initiallySelected);
      }
    });

  }, []);

  return (
    
    <>    
        <RemoveDialog
          open={removeRequest.state != "complete" && removeRequest.state != "cancelled"}
          removeRequest={removeRequest}
          requestService={requestService}
          ontologyController={ontologyController}
        />

        <Router>
          <Switch>
            <Route path="/signin">
              <SignInPage />
            </Route>
            <Route path="/accounts">
              <AccountManagementPage 
                requestService={requestService}
               />
            </Route>
            <Route path="/">

              {authFormOpen && <AmplifyAuthenticator />}
              <MainPage
                authController={authController}
                ontologyController={ontologyController}
                requestService={requestService}
              >
              </MainPage>
            </Route>
          </Switch>
        </Router>
      </>


  );
}

export default App;
