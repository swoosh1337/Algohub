import { useState } from "react";

//Credit: https://designcode.io/react-hooks-handbook-useinput-hook

const useInput = (initialValue) => {
    const [value, setValue] = useState(initialValue);

    const handleChange = (event) => {
        setValue(event.target.value);
    };

    return {
        value,
        onChange: handleChange
    };
};

export default useInput;