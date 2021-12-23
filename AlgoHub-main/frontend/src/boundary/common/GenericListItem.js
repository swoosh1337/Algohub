
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
    margin-right: 4px;
    margin-left: 4px;
    margin-bottom: 4px;
    
    
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
    padding: 5px;
    font-size: 10pt;
    background-color: ${props => props.selected ? "lightgray" : "white"};
    height: fit-content;
    min-height: 30px;   
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


export default function GenericListItem(props) {
  
    return (

      <Wrapper onClick={() => props.onSelected(props.item)} selected={props.selected}>
          <ContentWrapper >
            <TitleWrapper>{props.title}</TitleWrapper>
            { props.enableRemove &&
              <IconButton onClick={() => props.onRemove(props.item)} color="inherit" size="small">
                  <HighlightOffIcon/>
              </IconButton>
            }
            
          </ContentWrapper>
      </Wrapper>
  );
}

