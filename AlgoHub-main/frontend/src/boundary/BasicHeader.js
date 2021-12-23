
import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import styled from 'styled-components';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import AccessTimeIcon from '@mui/icons-material/AccessTime';

import { useHistory } from 'react-router-dom';

const Wrapper = styled.div`

    color: rgba(0, 0, 0, 0.87);
    box-shadow: 0px 2px 4px -1px rgb(0 0 0 / 20%), 0px 4px 5px 0px rgb(0 0 0 / 14%), 0px 1px 10px 0px rgb(0 0 0 / 12%);
    background-color: #b8212f;
    color: #fff;

`;

export default function Header(props) {

  const history = useHistory();

  return (
      <Wrapper>
      <Toolbar variant="dense">
      <IconButton onClick={() => history.push('/')} color="inherit">
           <ArrowBackIcon/>
          </IconButton>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            {props.title}
          </Typography>

          
        </Toolbar>
      </Wrapper>
  );
}

