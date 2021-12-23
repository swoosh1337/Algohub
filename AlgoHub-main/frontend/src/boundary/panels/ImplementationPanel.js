
import React, { useState } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import styled from 'styled-components';
import CircularProgress from '@mui/material/CircularProgress';
import Amplify, { Auth, API, Storage } from 'aws-amplify';
import SyntaxHighlighter from 'react-syntax-highlighter';
import { docco } from 'react-syntax-highlighter/dist/esm/styles/hljs';
import awsconfig from '../../aws-exports';
import { Config } from "../common/Config"
import axios from 'axios';
import { saveAs } from "file-saver";
import fileDownload from 'js-file-download'
import { useSelector, useDispatch } from 'react-redux'

const Wrapper = styled.div`

  
    box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
    border-radius: 5px;
    border-width: 1px;
    overflow-wrap: anywhere;
    margin: 5px;
    margin-bottom: 0px;
    display: flex;
    flex-direction: column;
    height: 100%;
    overflow: scroll;
`;

const ButtonWrapper = styled.div`

    padding-right: 10px;
    padding-bottom: 10px;
    display: flex;
    justify-content: right;

`;

export default function ImplementationPanel(props) {

    const [sourceCode, setSourceCode] = useState("")
    const [loadingFile, setLoadingFile] = useState(false)
    const [fileCache, setFileCache] = useState({});

    const implementation = useSelector(state => (state.viewModel.selectedItem["implementation"]))

    React.useEffect(() => {

        setSourceCode("")
        setLoadingFile(true)

        var cache = { ...fileCache }

        if (!implementation)
            return

        var filename = implementation.filename;

        try {

            if (cache[filename] === undefined) {

                if (implementation.filename) {
                    
                    axios.get(Config.S3_PATH + "implementations/" + filename)
                    .then(res => {
                        if (res.data) {
                            setSourceCode(res.data)
                            cache[filename] = res.data
                            setLoadingFile(false)
                        }
                    }).catch(() => {
                        setSourceCode("-Failed to retrieve implementation file")
                        setLoadingFile(false)
                    })
                }
            } else {

                setSourceCode(cache[filename])
                setLoadingFile(false)

            }

        } catch (exception) {

            setSourceCode("-Failed to retrieve implementation file")
            setLoadingFile(false)
        }

        setFileCache(cache)

    }, [implementation]);

    var lang = (implementation) ? implementation.programmingLanguage.toLowerCase() : "text"

    var handleDownload = (url, filename) => {

        const guidRegex = /.*(\{){0,1}[0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12}(\}){0,1}_/;
        filename = filename.replace(guidRegex, "");

        axios.get(url, {
            responseType: 'blob',
        })
        .then((res) => {
            fileDownload(res.data, filename)
        })
    }

    return (

        <Wrapper>

            {(loadingFile || !implementation) && <CircularProgress />}

            <SyntaxHighlighter language={lang} customStyle={{ height: "100%", margin: "0px", display: "flex", backgroundColor: "white" }} style={{ ...docco }}>
                {sourceCode}
            </SyntaxHighlighter>

            <ButtonWrapper>
                <Button
                    onClick={() => {
                        var filename = Config.S3_PATH + "implementations/" + implementation.filename;

                        if (filename) {
                            handleDownload(filename, implementation.filename)
                        }
                    }}
                    size="small"
                    variant="contained"
                >DOWNLOAD</Button>

            </ButtonWrapper>

        </Wrapper>
    );
}

