
import * as React from 'react';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import styled from 'styled-components';

import AccessTimeIcon from '@mui/icons-material/AccessTime';
import AccountTreeIcon from '@mui/icons-material/AccountTree';
import VpnKeyIcon from '@mui/icons-material/VpnKey';
import VisibilityIcon from '@mui/icons-material/Visibility';
import SupervisorAccountIcon from '@mui/icons-material/SupervisorAccount';

import LoginIcon from '@mui/icons-material/Login';
import LogoutIcon from '@mui/icons-material/Logout';

import { styled as material_styled} from '@mui/material/styles';
import DriveFileMoveIcon from '@mui/icons-material/DriveFileMove';

import { useHistory } from 'react-router-dom';
import Amplify, { Auth } from 'aws-amplify';
import { useSelector, useDispatch } from 'react-redux';

const Wrapper = styled.div`

    color: rgba(0, 0, 0, 0.87);
    box-shadow: 0px 2px 4px -1px rgb(0 0 0 / 20%), 0px 4px 5px 0px rgb(0 0 0 / 14%), 0px 1px 10px 0px rgb(0 0 0 / 12%);
    background-color: #b8212f;
    color: #fff;

`;

export default function Header(props) {

  const history = useHistory();
  const currentUser = useSelector(state => state.model.currentUser);

  return (
      <Wrapper>
      <Toolbar variant="dense">

          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            AlgoHub 
            {currentUser && " - " + currentUser.username} 
            {currentUser && currentUser.groups && currentUser.groups.length > 0 && "(" + currentUser.groups.join(",") + ")"}
          </Typography>
          <IconButton onClick={props.onClickBenchmarkMenu} color="inherit">
           <AccessTimeIcon/>
          </IconButton>
          { currentUser && 
            <IconButton onClick={() => history.push('/accounts')} color="inherit">
            <VisibilityIcon/>
            </IconButton>
          }

          { !currentUser && 
              <Button onClick={() => props.authController.login()}  color="inherit">
                Login
              </Button>
          }

          { currentUser && 
              <Button onClick={() => props.authController.logout()}  color="inherit">
                Logout
              </Button>
          }

          
          
        </Toolbar>
      </Wrapper>

  );
}

