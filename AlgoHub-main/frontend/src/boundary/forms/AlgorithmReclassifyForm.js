import React, { useState } from 'react';
import PropTypes from 'prop-types';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import Typography from '@mui/material/Typography';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import TextField from '@mui/material/TextField';
import LoadingButton from '@mui/lab/LoadingButton';
import Autocomplete from '@mui/material/Autocomplete';
import { useSelector, useDispatch } from 'react-redux';
import ListInput from "./ListInput";
import { isNumeric,powerOfTwo,validateStr, validateNum } from '../common/Common';
import { Form, useForm } from '../hooks/useForm';
const BootstrapDialog = styled(Dialog)(({ theme }) => ({
    '& .MuDialogContent-root': {
        padding: theme.spacing(2),
    },
    '& .MuDialogActions-root': {
        padding: theme.spacing(1),
    },
    "& .MuiPaper-root" : {
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

const initialFValues = {
    algorithm: null,
    parentClassification: null

}


export default function AlgorithmReclassifyForm(props) {

    const [loading, setLoading] = React.useState(false);
    const [submitDisabled, setSubmitDisabled] = useState(false);
    const [requestError, setRequestError] = useState("");

    const algorithmOptions = useSelector(state => (state.model.ontologyHierarchy || []).filter((item) => item.typeName == "algorithm") )
    const classificationOptions = useSelector(state => (state.model.ontologyHierarchy || []).filter((item) => item.typeName == "classification") )

    const validate = (fieldValues = values) => {

        let temp = { ...errors }

        validateStr(fieldValues, temp, "algorithmName", 100)
        validateStr(fieldValues, temp, "parentClassification")

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


            props.requestService.executePostRequest(
                (err) => {
                    setRequestError(err)
                    setLoading(false)
                    setSubmitDisabled(false)

                    if (err.length == 0) {

                        props.onClose()
                    }

                },
                {
                    algorithmId: (values.algorithm) ? values.algorithm.id : null,
                    newClassificationId: (values.parentClassification) ? values.parentClassification.id : null
                },
                "algorithms/reclassify",
                "Failed to reclassify algorithm.",
                "Reclassified algorithm successfully!"
                );

            console.log(values)
        }
    }

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
                    Algorithm Reclassify Form
                </BootstrapDialogTitle>
                <DialogContent dividers>
                    <GeneralInfo>

                        <ListInput

                            label="Algorithm Name"
                            name="algorithm"
                            value={values.algorithm}
                            sx={{width: "30%"}}
                            options={algorithmOptions}
                            error={errors.algorithm}
                            onChange={handleInputChange}

                        />
                        <ListInput

                            label="New Classification"
                            name="parentClassification"
                            value={values.parentClassification}
                            sx={{width: "30%"}}
                            options={classificationOptions}
                            error={errors.parentClassification}
                            onChange={handleInputChange}

                        />
                    </GeneralInfo>


                </DialogContent>
                <DialogActions>
                    <LoadingButton
                        onClick={handleSubmit}
                        loading={loading}
                        disabled={submitDisabled}
                        loadingPosition="center"
                        variant="contained"
                    >
                        Reclassify
                    </LoadingButton>
                </DialogActions>
            </BootstrapDialog>
        </div>
    );
}