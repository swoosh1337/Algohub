//Credit: https://github.com/CodAffection/Material-UI-Form-Design-and-Validation/blob/master/src/components/controls/Input.js

import React from 'react'
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { CircularProgress } from '@mui/material';
export default function ListInput(props) {

    const { disabled, sx = {}, name, options, label, item, value, error = null, onChange, onChangeItem } = props;
    return (

        <Autocomplete

            disabled={disabled}
            sx={sx}
            disablePortal
            id="combo-box-demo"
            getOptionLabel={(item) => item.name}
            options={options}
            renderInput={(params) => <><TextField
                variant="outlined"
                {...params}
                name={name}
                label={label}
                {...(error && {error:true,helperText:error})}

            />

            {disabled && <CircularProgress/>}
            </>
            
            }
  
            onChange={(e, val) => onChange(name, val)}
            value={value}


        />
    )
}