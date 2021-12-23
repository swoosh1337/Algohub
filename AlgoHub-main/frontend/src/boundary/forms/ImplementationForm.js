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
import useInput from "../hooks/useInput";
import {decode as base64_decode, encode as base64_encode} from 'base-64';
import LoadingButton from '@mui/lab/LoadingButton';
import SaveIcon from '@mui/icons-material/Save';
import SendIcon from '@mui/icons-material/Send';
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
    marginBottom: "50px",
    gap: "40px"

}));

const SourceCodeUpload = styled('div')(({ theme }) => ({
    
    display: "flex",
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: "50px"

}));

const initialFValues = {
    parent: null,
    language: ""
}

export default function ImplementationForm(props) {

    const [fileUploadMsg, setFileUploadMsg] = useState("") 
    const [fileContents, setFileContents] = useState("")
    const [requestError, setRequestError] = useState("")
    const [submitDisabled, setSubmitDisabled] = useState(false);
    const [loading, setLoading] = React.useState(false);
    const [filename, setFilename] = useState("");


    const validate = (fieldValues = values) => {

        let temp = { ...errors }

        if ('language' in fieldValues) {

            temp.language = fieldValues.language ? "" : "This field is required. ";
            temp.language += !(fieldValues.language.length > 100) ? "" : "Programming language name must be less then 100 characters.";
        }

        if ('parent' in fieldValues) {

            temp.parent = (fieldValues.parent) ? "" : "Implementation must have parent algorithm.";
        }

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


    function cleanString(input) {
        var output = "";
        for (var i=0; i<input.length; i++) {
            if (input.charCodeAt(i) <= 127) {
                output += input.charAt(i);
            }
        }
        return output;
    }

    var processFileUpload = async (e) => {
        e.preventDefault()
        const reader = new FileReader()
        reader.onload = async (e) => { 
          
            var text = (e.target.result)

            try {

                text = cleanString(text)

                setFileContents(base64_encode(text))
                setSubmitDisabled(false)
                setRequestError("")

            } catch(e) {

                setFileUploadMsg("Failed to upload file. " + e)
                setSubmitDisabled(true)
                setRequestError("Please upload a valid file")
            }
        };

        if(e.target.files[0]) {

            setFileUploadMsg(e.target.files[0].name)
            setFilename(e.target.files[0].name)
            reader.readAsText(e.target.files[0])
        }
    }

    const handleSubmit = e => {

        e.preventDefault()

        if(fileContents == "") {
            setFileUploadMsg("Please upload file.")
            setSubmitDisabled(true)
        }

        if (validate() && fileContents !="") {

            setLoading(true)
            setSubmitDisabled(true)
            setRequestError("")

            var fileExt = filename.split('.').pop();

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
                    sourceCodeBase64: fileContents,
                    name: values.language,
                    algorithmId: values.parent.id,
                    algorithmName: values.parent.name,
                    extension: fileExt
                },
                "implementations/add",
                "Failed to create algorithm.",
                "Created algorithm successfully!"
            );
        }
    }

    var algorithmOptions = useSelector(state => (state.model.ontologyHierarchy || []).filter((item) => item.typeName == "algorithm"));

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
                    Implementation Form
                </BootstrapDialogTitle>
                <DialogContent dividers>
                    <GeneralInfo>
 
                        <ListInput

                            label="Parent Algorithm"
                            name="parent"
                            value={values.parent}
                            sx={{width: "50%", marginRight: "50px" }}
                            options={algorithmOptions}
                            error={errors.parent}
                            onChange={handleInputChange}

                        />
                        <Input
                            label="Language"
                            name="language"
                            value={values.language}
                            error={errors.language}
                            sx={{ width: "50%"}}
                            onChange={handleInputChange}
                        />
                    </GeneralInfo>

                    <SourceCodeUpload>
                        <label htmlFor="source_code_upload">
                            <input
                                style={{ display: 'none' }}
                                id="source_code_upload"
                                name="source_code"
                                type="file"
                                onChange={(e) => processFileUpload(e)}
                                
                            />
                            <Button color="primary" variant="contained" component="span">
                                Upload Code
                            </Button>
                            {fileUploadMsg}
                        </label>
                    </SourceCodeUpload>

                </DialogContent>
                <font color="red">{ requestError.length > 0 && requestError}</font>
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