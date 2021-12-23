
import React, { useState } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import styled from 'styled-components';
import { Config } from "../common/Config"
import { CircularProgress } from '@mui/material';
import {updateCachedSet, updateRemoveRequest} from '../../model/ViewModel'
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import { useSelector, useDispatch } from 'react-redux'
import { Resizable } from "re-resizable";
import axios from 'axios';
import fileDownload from 'js-file-download'

const Wrapper = styled.div`

    border-radius: 5px;
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
    margin: 5px;
    padding: 5px;
    font-size: 10pt;
    margin-bottom: 0px;
    height: 100%;
    flex-direction: column;
    overflow: scroll;
    display:flex;
    flex-grow: 1;
    overflow-y: auto;

    margin-top: 15px;
`;

const Title = styled.h4`

    margin-bottom: 10px;
    text-align: center;
`;

const ProblemInstanceWrapper = styled.div`
    border-radius: 5px;
    height: 80px;
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
    margin-top: 10px;
    padding: 10px;
    display:flex;
    flex-direction: row;
    justify-content: space-between;
`;

const MsgWrapper = styled.h3`

    text-align: center;
`;

const ButtonWrapper = styled.div`
    justify-content: center;
    display: flex;
`;


export default function ProblemInstancePanel(props) {

    var selectedItem = useSelector(state => state.viewModel.selectedOntologyItem);
    var currentUser = useSelector(state => state.model.currentUser);
    const dispatch = useDispatch();
    const problemInstances = useSelector(state => state.viewModel.cachedSets["problem_instance"]);

    const [loadingProblemInstances, setLoadingProblemInstances] = useState(false);

    var handleDownload = (url, filename) => {

        const guidRegex = /.*(\{){0,1}[0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12}(\}){0,1}_/;
        filename = filename.replace(guidRegex, "");

        axios.get(url, {
            responseType: 'blob',
        })
        .then((res) => {
            fileDownload(res.data, filename)
        })
    }

    var updateProblemInstances = () => {
        
        dispatch(updateCachedSet({
            name: "problem_instance",
            state: []
        }))
        setLoadingProblemInstances(true)

        props.requestService.executePostRequest(
            (err, data) => {

                setLoadingProblemInstances(false)

                if (err.length == 0) {

                    dispatch(updateCachedSet({
                        name: "problem_instance",
                        state: data.problemInstances
                    }))
                }

            },
            {
                id: selectedItem.id
            },
            "problemInstances/by_algorithm",
            "",
            "",
            false
        );
    }

    var deleteProblemInstance = (problemInstance) => {

        dispatch(updateRemoveRequest(
            {
                msg: "Are you sure you want to remove problem instance?",
                item: {
                    id: problemInstance.id,
                    typeName: "problem_instance"
                },
                initiator: "problem_instance_panel"
            }
        ))
    }

    if(!problemInstances) {

        if(selectedItem) {

            updateProblemInstances();
        }
    }

    React.useEffect(() => {

        if(selectedItem && selectedItem.typeName == "algorithm") {
            updateProblemInstances();

        } else {
            dispatch(updateCachedSet({
                name: "problem_instance",
                state: []
            }))
        }
    
    }, [selectedItem]);

    return (

        <Wrapper>
            <Typography variant="h6" align="center" component="div" gutterBottom>
                Problem Instances
            </Typography>
            {currentUser &&
                <ButtonWrapper>
                    <IconButton color="inherit" size="large" onClick={() => props.togglePanel("problem_instance_add_form")}>
                        <AddCircleOutlineIcon />
                    </IconButton>
                </ButtonWrapper>
            }
            {loadingProblemInstances && <CircularProgress />}
            {
               problemInstances && problemInstances.map((item) => {


                    return <ProblemInstanceWrapper>

                        <table>

                            <tr>
                                <td><i>ProblemType:</i></td>
                                <td>{item.problemType}</td>
                            </tr>
                            <tr>
                                <td><i>Input Size:</i></td>
                                <td>{item.datasetSize}</td>
                            </tr>
                            <tr>
                                <td>
                                <Button
                                    onClick={() => {
                                        var filename = Config.S3_PATH + "datasets/" + item.datasetFilename;

                                        if (filename) {
                                            handleDownload(filename, item.datasetFilename)
                                        }
                                    }}
                                    size="small"
                                    variant="contained"
                                >DOWNLOAD</Button>
                                </td>
                            </tr>
                        </table>
                        <br />

                        {currentUser &&

                            <ButtonWrapper>
                                <IconButton color="inherit" size="small">
                                    <HighlightOffIcon onClick={() => deleteProblemInstance(item)}/>
                                </IconButton>
                            </ButtonWrapper>
                        }
                    </ProblemInstanceWrapper>
                })
            }

            {problemInstances && problemInstances.length == 0 && !loadingProblemInstances && <MsgWrapper>None</MsgWrapper>}

        </Wrapper>
    );
}

