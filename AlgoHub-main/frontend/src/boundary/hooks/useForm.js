//Credit: https://github.com/CodAffection/Material-UI-Form-Design-and-Validation/blob/master/src/components/useForm.js

import React, { useState } from 'react'
import TextField from '@mui/material/TextField';

export function useForm(initialFValues, validateOnChange = false, validate) {


    const [values, setValues] = useState(initialFValues);
    const [errors, setErrors] = useState({});

    const handleInputChange = (name, value) => {

        console.log(name, value)

        setValues({
            ...values,
            [name]: value
        })
        if (validateOnChange)
            validate({ [name]: value })
    }

    const resetForm = () => {
        setValues(initialFValues);
        setErrors({})
    }


    return {
        values,
        setValues,
        errors,
        setErrors,
        handleInputChange,
        resetForm

    }
}

export function Form(props) {

    const { children, ...other } = props;
    return (
        <form style={
            {
                width: "100%",
                height: "100%"
            
            }} autoComplete="off" {...other}>
            {props.children}
        </form>
    )
}