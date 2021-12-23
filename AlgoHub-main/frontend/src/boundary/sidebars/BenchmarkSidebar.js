
import React, { useState } from 'react';
import styled from 'styled-components';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import IconButton from '@mui/material/IconButton';
import BenchmarkEntry from '../common/BenchmarkEntry';
import { Resizable } from "re-resizable";
import { useSelector, useDispatch } from 'react-redux'
import { CircularProgress } from '@mui/material';
import { updateCachedSet, updateRemoveRequest } from '../../model/ViewModel'
const BenchmarkSidebarWrapper = styled.div`
    user-select: none;
    width: 20%;
    height: 100%;
    background-color: #f5f7fa;
    max-width: ${props => props.open ? "20%" : "0%"};
    min-width: ${props => props.open ? "20%" : "0%"};
    transform: ${props => props.open ? "translateX(0%)" : "translateX(100%)"};
    transition: transform 250ms ease-in-out, max-width 250ms, min-width 250ms;
    overflow:hidden;
    border-radius: 10px 5px 10px 5px;
    box-shadow: 0px 2px 4px -1px rgb(0 0 0 / 20%), 0px 4px 5px 0px rgb(0 0 0 / 14%), 0px 1px 10px 0px rgb(0 0 0 / 12%);
    margin-top: 3px;
    overflow: scroll;
    overflow-y: auto;
`;

const ButtonWrapper = styled.div`
    justify-content: center;
    display: flex;
    height: 4%;
    border-radius: 0px 10px 10px 5px;
    border: 1px solid #ac2b37;
    margin-bottom: 10px;
`;

const MsgWrapper = styled.h3`

    text-align: center;
`;

export default function BenchmarkSidebar(props) {

    var selectedItem = useSelector(state => state.viewModel.selectedOntologyItem);
    var currentUser = useSelector(state => state.model.currentUser);
    const dispatch = useDispatch();
    const benchmarks = useSelector(state => state.viewModel.cachedSets["benchmark"]);

    const [loadingBenchmarks, setLoadingBenchmarks] = useState(false);

    var updateBenchmarks = () => {

        dispatch(updateCachedSet({
            name: "benchmark",
            state: []
        }))
        setLoadingBenchmarks(true)

        props.requestService.executePostRequest(
            (err, data) => {

                setLoadingBenchmarks(false)

                if (err.length == 0) {

                    dispatch(updateCachedSet({
                        name: "benchmark",
                        state: data.benchmarks
                    }))
                }

            },
            {
                id: selectedItem.id
            },
            "benchmarks/by_implementation",
            "",
            "",
            false
        );
    }

    var deleteBenchmark = (benchmark) => {

        dispatch(updateRemoveRequest(
            {
                msg: "Are you sure you want to remove benchmark?",
                item: {
                    id: benchmark.id,
                    typeName: "benchmark"
                },
                initiator: "benchmark_sidebar"
            }
        ))
    }

    if (!benchmarks) {

        if (selectedItem) {

            updateBenchmarks();
        }
    }

    React.useEffect(() => {

        if (selectedItem && selectedItem.typeName == "implementation") {
            updateBenchmarks();

        } else {
            dispatch(updateCachedSet({
                name: "benchmark",
                state: []
            }))
        }

    }, [selectedItem]);

    return (

            <BenchmarkSidebarWrapper open={props.open}>


                <ButtonWrapper>
                    <IconButton color="inherit" size="large" onClick={() => props.togglePanel("benchmark_add_form", true)}>
                        <AddCircleOutlineIcon />
                    </IconButton>
                </ButtonWrapper>

                {!benchmarks || benchmarks.length == 0 && !loadingBenchmarks && <MsgWrapper>No Benchmarks</MsgWrapper>}
                {loadingBenchmarks && <CircularProgress />}
                {benchmarks && benchmarks.map((item) => {

                    return <BenchmarkEntry onClick={() => deleteBenchmark(item)} enableRemove={currentUser} benchmark={item} />
                })
                }

            </BenchmarkSidebarWrapper>
    )
}