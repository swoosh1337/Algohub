import React, { useState } from 'react';
import PropTypes from 'prop-types';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import LoadingButton from '@mui/lab/LoadingButton';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import Typography from '@mui/material/Typography';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { useSelector, useDispatch } from 'react-redux';
import { updateCachedSet } from '../../model/ViewModel';
import { Form, useForm } from '../hooks/useForm';
import Input from "./Input";
import ListInput from "./ListInput";
import { isNumeric, powerOfTwo, validateStr, validateNum } from '../common/Common';
import { CircularProgress } from '@mui/material';
const BootstrapDialog = styled(Dialog)(({ theme }) => ({
    '& .MuDialogContent-root': {
        padding: theme.spacing(2),
    },
    '& .MuDialogActions-root': {
        padding: theme.spacing(1),
    },
    "& .MuiPaper-root": {
        height: "fit-content"
    }
}));

const BootstrapDialogTitle = (props) => {
    const { children, onClose, ...other } = props;

    return (
        <DialogTitle sx={{ m: 0, p: 2 }} {...other}>
            {children}
            {onClose ? (
                <IconButton
                    aria-label="close"
                    onClick={onClose}
                    sx={{
                        position: 'absolute',
                        right: 8,
                        top: 8,
                        color: (theme) => theme.palette.grey[500],
                    }}
                >
                    <CloseIcon />
                </IconButton>
            ) : null}
        </DialogTitle>
    );
};


const FieldWrapper = styled('div')(({ theme }) => ({

    marginBottom: '50px'
}));

const GeneralInfo = styled('div')(({ theme }) => ({

    display: "flex",
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: "50px"

}));

const MachineInformation = styled('div')(({ theme }) => ({

    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    paddingBottom: "20px"

}));

const ResultsInfomation = styled('div')(({ theme }) => ({

    display: "flex",
    flexDirection: "row",
    justifyContent: "space-between",
    paddingTop: "50px"

}));

const initialFValues = {
    name: "",
    parentImplementation: null,
    parentProblemInstance: null,
    cpuName: "",
    cpuCores: null,
    cpuThreads: null,
    memory: null,
    l1Cache: null,
    l2Cache: null,
    l3Cache: null,
    execTime: null,
    memUsage: null
}

export default function BenchmarkForm(props) {

    const [loading, setLoading] = React.useState(false);
    const [submitDisabled, setSubmitDisabled] = useState(false);
    const [requestError, setRequestError] = useState("");
    const [loadingProblemInstances, setLoadingProblemInstances] = useState(false);
    const [problemInstanceOptions, setProblemInstanceOptions] = useState([]);

    const dispatch = useDispatch();

    const validate = (fieldValues = values) => {

        let temp = { ...errors }

        validateStr(fieldValues, temp, "parentImplementation")
        validateStr(fieldValues, temp, "cpuName")
        validateStr(fieldValues, temp, "cpuName")
        validateNum(fieldValues, temp, "cpuCores")
        validateNum(fieldValues, temp, "cpuThreads")
        validateNum(fieldValues, temp, "memory")
        validateNum(fieldValues, temp, "l1Cache")
        validateNum(fieldValues, temp, "l2Cache")
        validateNum(fieldValues, temp, "l3Cache")
        validateNum(fieldValues, temp, "execTime")
        validateNum(fieldValues, temp, "memUsage")

        setErrors({
            ...temp
        })

        if (fieldValues == values)
            return Object.values(temp).every(x => x == "")
    }

    const {
        values,
        setValues,
        errors,
        setErrors,
        handleInputChange,
        resetForm
    } = useForm(initialFValues, true, validate);

    const handleSubmit = e => {
        e.preventDefault()
        if (validate()) {

            setLoading(true)
            setSubmitDisabled(true)
            setRequestError("")

            console.log(values)

            props.requestService.executePostRequest(
                (err) => {
                    setRequestError(err)
                    setLoading(false)
                    setSubmitDisabled(false)

                    if (err.length == 0) {

                        dispatch(updateCachedSet({
                            name: "benchmark",
                            state: null
                        }));

                        props.onClose()
                    }

                },
                {
                    implementationId: (values.parentImplementation) ? values.parentImplementation.id : null,
                    problemInstanceId: (values.parentProblemInstance) ? values.parentProblemInstance.id : null,
                    memory: values.memory,
                    cpuName: values.cpuName,
                    cpuThreads: values.cpuThreads,
                    cpuCores: values.cpuCores,
                    cpuL1Cache: values.l1Cache,
                    cpuL2Cache: values.l2Cache,
                    cpuL3Cache: values.l3Cache,
                    executiontime: values.execTime,
                    memoryUsage: values.memUsage
                },
                "benchmarks/add",
                "Failed to create benchmark.",
                "Created benchmark successfully!",
                false
            );
        }
    }

    var implementationOptions = useSelector(state => (state.model.ontologyHierarchy || []).filter((item) => item.typeName == "implementation"));
    var algorithms = useSelector(state => (state.model.ontologyHierarchy || []).filter((item) => item.typeName == "algorithm"));

    var modifiedImplementationOptions = [...implementationOptions].map((item) => {
        return {...item, name: algorithms.filter((a) => a.id == item.parentId)[0].name + "-" + item.name}
    })



    React.useEffect(() => {

        if(values.parentImplementation) {

            var candidates = algorithms.filter((item) => item.id == values.parentImplementation.parentId)

            if (candidates.length > 0) {
    
                setLoadingProblemInstances(true)
    
                props.requestService.executePostRequest(
                    (err, data) => {
    
                        setLoadingProblemInstances(false)
    
                        if (err.length == 0) {
    
                            setProblemInstanceOptions(data.problemInstances.map((item) =>{
                                return {...item, name: item.problemType}
                            } ))
                        }
    
                    },
                    {
                        id: candidates[0].id
                    },
                    "problemInstances/by_algorithm",
                    "",
                    "",
                    false
                );
            }
        }

    }, [values.parentImplementation]);


    return (
        <div>
            <BootstrapDialog
                onClose={() => props.onClose()}
                aria-labelledby="customized-dialog-title"
                open={props.open}
                maxWidth="xl"
                fullWidth="true"
            >
                <BootstrapDialogTitle id="customized-dialog-title" onClose={() => props.onClose()}>
                    Benchmark Form
                </BootstrapDialogTitle>
                <DialogContent dividers>
                    <GeneralInfo>
                        <ListInput

                            label="Parent Implementation"
                            name="parentImplementation"
                            value={values.parentImplementation}
                            sx={{ width: "50%", marginRight: "50px" }}
                            options={modifiedImplementationOptions}
                            error={errors.parentImplementation}
                            onChange={handleInputChange}

                        />
                        <ListInput

                            label="Problem Instance"
                            name="parentProblemInstance"
                            value={values.parentProblemInstance}
                            sx={{ width: "50%", marginRight: "50px" }}
                            options={problemInstanceOptions}
                            error={errors.parentProblemInstance}
                            disabled={loadingProblemInstances}
                            onChange={handleInputChange}

                        />
                    </GeneralInfo>

                    <MachineInformation>

                        <Input
                            label="CPU Name"
                            name="cpuName"
                            value={values.cpuName}
                            error={errors.cpuName}
                            sx={{ width: "30%" }}
                            onChange={handleInputChange}
                        />

                        <Input
                            label="CPU Cores"
                            name="cpuCores"
                            value={values.cpuCores}
                            error={errors.cpuCores}
                            sx={{ width: "30%" }}
                            onChange={handleInputChange}
                        />

                        <Input
                            label="CPU Threads"
                            name="cpuThreads"
                            value={values.cpuThreads}
                            error={errors.cpuThreads}
                            sx={{ width: "30%" }}
                            onChange={handleInputChange}
                        />

                        <Input
                            label="Memory (MB)"
                            name="memory"
                            value={values.memory}
                            error={errors.memory}
                            sx={{ width: "30%" }}
                            onChange={handleInputChange}
                        />

                        <Input
                            label="L1 Cache (KB)"
                            name="l1Cache"
                            value={values.l1Cache}
                            error={errors.l1Cache}
                            sx={{ width: "30%" }}
                            onChange={handleInputChange}
                        />
                        <Input
                            label="L2 Cache (KB)"
                            name="l2Cache"
                            value={values.l2Cache}
                            error={errors.l2Cache}
                            sx={{ width: "30%" }}
                            onChange={handleInputChange}
                        />
                        <Input
                            label="L3 Cache (KB)"
                            name="l3Cache"
                            value={values.l3Cache}
                            error={errors.l3Cache}
                            sx={{ width: "30%" }}
                            onChange={handleInputChange}
                        />

                    </MachineInformation>

                    <ResultsInfomation>

                        <Input
                            label="Execution Time (ms)"
                            name="execTime"
                            value={values.execTime}
                            error={errors.execTime}
                            sx={{ width: "30%" }}
                            onChange={handleInputChange}
                        />
                        <Input
                            label="Memory Usage (KB)"
                            name="memUsage"
                            value={values.memUsage}
                            error={errors.memUsage}
                            sx={{ width: "30%" }}
                            onChange={handleInputChange}
                        />

                    </ResultsInfomation>


                </DialogContent>
                <font color="red">{requestError.length > 0 && requestError}</font>
                <DialogActions>

                    <LoadingButton
                        onClick={handleSubmit}
                        loading={loading}
                        disabled={submitDisabled}
                        loadingPosition="center"
                        variant="contained"
                    >
                        Create
                    </LoadingButton>
                </DialogActions>
            </BootstrapDialog>
        </div>
    );
}