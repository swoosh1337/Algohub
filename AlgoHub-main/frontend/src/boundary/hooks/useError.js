import { useState } from "react";


const useError = (validationRules) => {
    const [errorMsg, setErrorMsg] = useState("");

    const clear = () => {

        setErrorMsg("")
    }

    const update = () => {

        var error = ""

        for(var rule in validationRules) {

            if(!rule.check()) {

                error += rule.msg + "\n";
            }
        }

        setErrorMsg(console.error())
    }

    const isValid = ()=> {

        return errorMsg.length == 0
    }

    return {
        helperText: errorMsg,
        error: errorMsg.length > 0,
        onKeyUp: clear
    };
};

export default useError;