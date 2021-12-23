import '../../App.css';

import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import Header from '../Header';
import OntologySidebar from '../sidebars/OntologySidebar';
import BenchmarkSidebar from '../sidebars/BenchmarkSidebar';

import AlgorithmPanel from '../panels/AlgorithmPanel';
import ImplementationPanel from '../panels/ImplementationPanel';
import ProblemInstancePanel from '../panels/ProblemInstancePanel';

import ClassificationForm from '../forms/ClassificationForm';
import ClassificationMergeForm from '../forms/ClassificationMergeForm';
import AlgorithmRankingPanel from '../panels/AlgorithmRankingPanel';
import AlgorithmReclassifyForm from '../forms/AlgorithmReclassifyForm';
import BenchmarkForm from '../forms/BenchmarkForm';
import AlgorithmForm from '../forms/AlgorithmForm';
import ImplementationForm from "../forms/ImplementationForm"
import ProblemInstanceForm from '../forms/ProblemInstanceForm';
import { useSnackbar } from 'notistack';
import Typography from '@mui/material/Typography';
import { Resizable } from "re-resizable";
import { HeadphonesBatteryOutlined } from '@mui/icons-material';
import { useSelector,useDispatch } from 'react-redux';
import { setPanelVisibility, updateRemoveRequest } from "../../model/ViewModel";
import RemoveDialog from '../forms/RemoveDialog';

const Wrapper = styled.div`
        
    display: flex;
    width: 100%;
    height: 99%;
    flex-direction: column;
    min-width: 0;

`;

const ContentWrapper = styled.div`
    display: flex;
    width: 100%;
    height: 100%;
    flex-direction: row;
    min-height: 0; 

`;

const InnerContentWrapper = styled.div`
    display: flex;
    width: 100%;
    flex-direction: column;
    align-items: stretch;
    min-width: 0;
`;

const PanelTitle = styled.div`   
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
    border-radius: 5px;
    padding: 1px;
    border-width: 1px;
    margin: 5px;
    display: flex;
    justify-content: center;
    
`;

export default function MainPage(props) {


    var title = useSelector(state => state.viewModel.headerTitle);
    var selectedItemType = useSelector(state => state.viewModel.selectedOntologyItemType)
    var openPanels = useSelector(state => state.viewModel.openPanels);
    const dispatch = useDispatch();

    const [topPanelHeight, setTopPanelHeight] = useState(null);
    const innerContentElement = useRef(null)

    useEffect(() => {
        if(topPanelHeight == null)
            setTopPanelHeight(innerContentElement.current.clientHeight * 0.50)
    })

    
    var togglePanel = (name, state = undefined) => {

        if(state != undefined) {

            dispatch(setPanelVisibility(
                {
                    name: name,
                    state: state
                }
            ))
        } else {

            dispatch(setPanelVisibility(
                {
                    name: name,
                    state: !openPanels.includes(name)
                }
            ))
        }
    }

    return (

        <Wrapper>

            <Header
                authController={props.authController}
                onClickBenchmarkMenu={() => togglePanel("benchmark_menu")}
            />

            <ContentWrapper>

                <OntologySidebar
                    open={true}
                    ontologyController={props.ontologyController}
                    togglePanel={togglePanel}
                />
                
                <InnerContentWrapper ref={innerContentElement} >
                    <PanelTitle>
                        <Typography variant="h6" align="center" component="div" gutterBottom>
                            {title}
                        </Typography>
                    </PanelTitle>



                    {selectedItemType == "algorithm" && <Resizable
                        enable={{
                            top: false,
                            right: false,
                            bottom: true,
                            left: false,
                            topRight: false,
                            bottomRight: false,
                            bottomLeft: false,
                            topLeft: false
                        }}
                        size={{ height: (topPanelHeight == null) ? "0" : topPanelHeight}}
                        onResizeStop={(e, direction, ref, d) => {

                            setTopPanelHeight(topPanelHeight + d.height)
                        }}
                    >
                        <AlgorithmPanel
                            ontologyController={props.ontologyController}
                        />
                    </Resizable>}

                    {selectedItemType == "implementation" &&

                        <ImplementationPanel
                            ontologyController={props.ontologyController}
                            requestService={props.requestService}
                        />

                    }
                    {(selectedItemType == "algorithm" ) &&
                        <ProblemInstancePanel
                            ontologyController={props.ontologyController}
                            togglePanel={togglePanel}
                            requestService={props.requestService}
                        />
                    }


                </InnerContentWrapper>
                <BenchmarkSidebar
                    open={openPanels.includes("benchmark_menu")}
                    togglePanel={togglePanel}
                    requestService={props.requestService}
                />

            </ContentWrapper>
    

            <ClassificationForm
                open={openPanels.includes("classification_add_form")}
                onClose={() => togglePanel("classification_add_form", false)}
                requestService={props.requestService}
                onSubmit={props.addClassification}
            />

            <ProblemInstanceForm
                open={openPanels.includes("problem_instance_add_form")}
                onClose={() => togglePanel("problem_instance_add_form", false)}
                requestService={props.requestService}
            />

            <ClassificationMergeForm
                open={openPanels.includes("classification_merge_form")}
                onClose={() => togglePanel("classification_merge_form", false)}
                requestService={props.requestService}
            />

            <AlgorithmRankingPanel
                open={openPanels.includes("algorithm_ranking_panel")}
                onClose={() => togglePanel("algorithm_ranking_panel", false)}
                requestService={props.requestService}
            />

            <BenchmarkForm
                open={openPanels.includes("benchmark_add_form")}
                onClose={() => togglePanel("benchmark_add_form", false)}
                requestService={props.requestService}
            />

            <AlgorithmForm
                open={openPanels.includes("algorithm_add_form")}
                onClose={() => togglePanel("algorithm_add_form", false)}
                requestService={props.requestService}
            />

            <ImplementationForm

                open={openPanels.includes("implementation_add_form")}
                onClose={() => togglePanel("implementation_add_form", false)}
                requestService={props.requestService}
            />

            <AlgorithmReclassifyForm

                open={openPanels.includes("algorithm_reclassify_form")}
                onClose={() => togglePanel("algorithm_reclassify_form", false)}
                requestService={props.requestService}
            />

        </Wrapper>
    );

}