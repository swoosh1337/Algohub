
import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import styled from 'styled-components';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';

const Wrapper = styled.div`

    border-radius: 5px;
    margin: 5px;
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
    padding: 5px;
    font-size: 10pt;
    background-color: white;
    width: 93%;
    height: 30px;    
`;

const ContentWrapper = styled.div`

    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-right: 10px;
    width: 100%;

`;

const TitleWrapper = styled.div`

    justify-content: center;
`;


export default function UserEntry(props) {
  return (

      <Wrapper>
          <ContentWrapper onClick={props.onSelected}>
            <TitleWrapper>{props.title}</TitleWrapper>
            { props.enableRemove &&
              <IconButton color="inherit" size="small">
                  <HighlightOffIcon onClick={props.onRemove}/>
              </IconButton>
            }
            
          </ContentWrapper>
      </Wrapper>
  );
}

