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
import LoadingButton from '@mui/lab/LoadingButton';
import { useSelector, useDispatch } from 'react-redux';
import { Form, useForm } from '../hooks/useForm';
import Input from "./Input";
import ListInput from "./ListInput";

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


const GeneralInfo = styled('div')(({ theme }) => ({

    display: "flex",
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: "50px",
    width: "100%"

}));

const initialFValues = {
    name: "",
    id: null
}
export default function ClassificationForm(props) {

    const [loading, setLoading] = React.useState(false);
    const [submitDisabled, setSubmitDisabled] = useState(false);
    const [requestError, setRequestError] = useState("");

    const validate = (fieldValues = values) => {

        let temp = { ...errors }

        if ('name' in fieldValues) {

            temp.name = fieldValues.name ? "" : "This field is required. ";
            temp.name += !(fieldValues.name.length > 100) ? "" : "Classification name must be less then 100 characters.";

        }
        temp.id = "";

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
                    parentId: (values.id) ? values.id.id : null,
                    name: values.name
                },
                "classifications/add",
                "Failed to create implementation.",
                "Created implementation successfully!"
            );
        }
    }

    var parentClassificationOptions = useSelector(state => (state.model.ontologyHierarchy || []).filter((item) => item.typeName == "classification"));

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
                    Classification Form
                </BootstrapDialogTitle>
                <DialogContent dividers>

                    <Form onSubmit={handleSubmit}>
                        <GeneralInfo>
                            <ListInput

                                label="Parent Classification"
                                name="id"
                                value={values.id}
                                sx={{width: "50%", marginRight: "50px" }}
                                options={parentClassificationOptions}
                                error={errors.id}
                                onChange={handleInputChange}

                            />
                            <Input
                                label="Classification Name"
                                name="name"
                                value={values.name}
                                error={errors.name}
                                sx={{ width: "50%"}}
                                onChange={handleInputChange}
                            />

                        </GeneralInfo>
                    </Form>


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