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
import Autocomplete from '@mui/material/Autocomplete';
import { useSelector, useDispatch } from 'react-redux'
import { Form, useForm } from '../hooks/useForm';
import Input from "./Input";
import ListInput from "./ListInput";
import LoadingButton from '@mui/lab/LoadingButton';
import { isNumeric, powerOfTwo, validateStr, validateNum } from '../common/Common';

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

const initialFValues = {
    classificationA: null,
    classificationB: null
}

export default function ClassificationMergeForm(props) {

    const [loading, setLoading] = React.useState(false);
    const [submitDisabled, setSubmitDisabled] = useState(false);
    const [requestError, setRequestError] = useState("");

    var classificationOptions = useSelector(state => (state.model.ontologyHierarchy || []).filter((item) => item.typeName == "classification"));

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

                        props.onClose();

                    } else {

                        setRequestError(err)
                        setLoading(false)
                        setSubmitDisabled(false)
                    }

                },
                {
                    sourceId: values.classificationA.id,
                    targetId: values.classificationB.id

                },
                "classifications/merge",
                "Failed to merge classifications.",
                ""
            );
        }
    }


    const validate = (fieldValues = values) => {

        let temp = { ...errors }

        validateStr(fieldValues, temp, "classificationA");
        validateStr(fieldValues, temp, "classificationB");

        setRequestError("")

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
                    Classification Merge Form
                </BootstrapDialogTitle>
                <DialogContent dividers>

                    <GeneralInfo>
                        Merge
                        <ListInput

                            label="Classification A"
                            name="classificationA"
                            value={values.classificationA}
                            sx={{ width: "50%", marginRight: "10px", marginLeft: "10px" }}
                            options={classificationOptions}
                            error={errors.classificationA}
                            onChange={handleInputChange}

                        />
                        Into
                        <ListInput

                            label="Classification B"
                            name="classificationB"
                            value={values.classificationB}
                            sx={{ width: "50%", marginRight: "50px", marginLeft: "10px" }}
                            options={classificationOptions}
                            error={errors.classificationB}
                            onChange={handleInputChange}

                        />
                    </GeneralInfo>


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
                        Merge
                    </LoadingButton>
                </DialogActions>
            </BootstrapDialog>
        </div>
    );
}