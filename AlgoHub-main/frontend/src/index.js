import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { SnackbarProvider } from "notistack";
import store from './model/ModelProxy';
import { Provider } from 'react-redux'
import { createTheme, ThemeProvider } from '@mui/material/styles';

export const themeOptions = createTheme({
  palette: {
    type: 'light',
    primary: {
      main: '#b8212f',
    },
    secondary: {
      main: '#f50057',
    },
  },
});


ReactDOM.render(
  <ThemeProvider theme={themeOptions}>

    <SnackbarProvider
      maxSnack={10}
      preventDuplicate
    >
      <Provider store={store}>
        <App />
      </Provider>
    </SnackbarProvider>
  </ThemeProvider>,
  document.getElementById('root')
);

