
import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import styled from 'styled-components';


import BasicHeader from '../BasicHeader';

const Wrapper = styled.div`

    width: 100%;
    height: 100%;
`;

export default function SignInPage(props) {
  return (

      <Wrapper>
          <BasicHeader title="Sign In"/>
      </Wrapper>
  );
}

