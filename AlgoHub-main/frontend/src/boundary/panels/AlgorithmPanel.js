
import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import styled from 'styled-components';
import CircularProgress from '@mui/material/CircularProgress';
import { useSelector, useDispatch } from 'react-redux'
const Wrapper = styled.div`
    height: 100%;
    border-radius: 5px;
    border-width: 1px;
    font-size: 25pt;
    display: flex;
    flex-grow: 1;
    margin: 5px;
`;


const AlgorithmDescription = styled.div`
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
    flex-grow: 1;
    border-radius: 5px;
    font-size: 12pt;
    border-width: 1px;
    overflow-wrap: anywhere;
    padding-left: 5px;
    padding-right: 5px;
    display: flex;
    flex-direction: column;
    overflow-y: scroll;
    
`;

export default function AlgorithmPanel(props) {

    const algorithm = useSelector(state => state.viewModel.selectedItem["algorithm"]);

    return (

        <Wrapper>
            

            <AlgorithmDescription>   
            <Typography variant="h6" align="center" component="div" gutterBottom>
                Algorithm Description
            </Typography>
                {!algorithm && <CircularProgress/>}{algorithm && algorithm.description}
            </AlgorithmDescription>
            
        </Wrapper>
    );
}

