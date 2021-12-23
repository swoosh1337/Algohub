//Credit: https://github.com/CodAffection/Material-UI-Form-Design-and-Validation/blob/master/src/components/controls/Input.js

import React from 'react'
import TextField from '@mui/material/TextField';
export default function Input(props) {

    const { multiline = false, rows=0, sx, name, label, value,error=null, onChange } = props;
    return (
        <TextField
            variant="outlined"
            label={label}
            name={name}
            multiline={multiline}
            rows={rows}
            value={value}
            sx={{...sx, marginBottom: "10px"}}
            onChange={(e) => {

                const {name, value} = e.target;
                onChange(name, value)
            
            }}
            {...(error && {error:true,helperText:error})}
        />
    )
}