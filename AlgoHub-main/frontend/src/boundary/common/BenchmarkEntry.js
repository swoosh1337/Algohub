
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
    text-align: center;
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
    padding: 5px;
    font-size: 10pt;
    background-color: white;
  
    
    `;

const InputSize = styled.div`

    border-radius: 25px;
    margin: 5px;
    text-align: left;
    
`;

const MachineInfo = styled.div`

    border-radius: 25px;
    margin: 5px;
    text-align: left;
  
`;

const Results = styled.div`

    border-radius: 25px;
    margin: 5px;
    text-align: left;
    
`;

const ContentWrapper = styled.div`

    display: flex;
    justify-content: space-between;

`;


export default function BenchmarkEntry(props) {
  return (

      <Wrapper>
          <ContentWrapper>

            <InputSize>
                        <i>Input Size</i>: {props.benchmark.datasetSize} 
                        <br/>
                        <i>Problem Instance</i>: {props.benchmark.problemType}</InputSize>
            
            { props.enableRemove &&
              <IconButton color="inherit" size="small">
                  <HighlightOffIcon onClick={props.onClick} />
              </IconButton>
            }
            
          </ContentWrapper>
          <MachineInfo>
            <h4>Machine Information</h4>
            <i>CPU</i>: {props.benchmark.cpuName}
            <br/>
            <i>CPU Cores</i>: {props.benchmark.cpuCores}
            <br/>
            <i>CPU Threads</i>: {props.benchmark.cpuThreads}
            <br/>
            <i>CPU L1 Cache (KB)</i>: {props.benchmark.cpuL1Cache}
            <br/>
            <i>CPU L2 Cache (KB)</i>: {props.benchmark.cpuL2Cache}
            <br/>
            <i>CPU L3 Cache (KB)</i>: {props.benchmark.cpuL3Cache}
            <br/>
            <i>Memory (MB)</i>: {props.benchmark.memory}
          </MachineInfo>
          <Results>
            <h4>Results</h4>
            <i>Memory Usage (KB): </i>: {props.benchmark.memoryUsage}
            <br/>
            <i>Execution Time (ms): </i>: {props.benchmark.executiontime}
            <br/>
            <i>Date: </i>: {props.benchmark.executionDate}
          </Results>
      </Wrapper>
  );
}

