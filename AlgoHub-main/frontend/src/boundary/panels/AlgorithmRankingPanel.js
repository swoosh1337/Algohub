import * as React from 'react';
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

const BootstrapDialog = styled(Dialog)(({ theme }) => ({

    '& .MuDialogContent-root': {
        padding: theme.spacing(2),
    },
    '& .MuDialogActions-root': {
        padding: theme.spacing(1),
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

const AlgorithmEntry = styled('div')({

    borderRadius: "10px 5px 10px 5px",
    boxShadow: "0px 2px 4px -1px rgb(0 0 0 / 20%), 0px 4px 5px 0px rgb(0 0 0 / 14%), 0px 1px 10px 0px rgb(0 0 0 / 12%)",
    width: "100%",
    height: "20px",
    marginTop: "10px"
  });
  

export default function AlgorithmRankingPanel(props) {

    return (
        <div>
            <BootstrapDialog
                onClose={() => props.onClose()}
                aria-labelledby="customized-dialog-title"
                open={props.open}
                fullWidth={true}
                maxWidth="xl"
            >
                <BootstrapDialogTitle id="customized-dialog-title" onClose={() => props.onClose()}>
                    Algorithm Rankings
                </BootstrapDialogTitle>
                <DialogContent dividers>
                    <AlgorithmEntry>Algorithm 1, num stars(4)</AlgorithmEntry>
                    <AlgorithmEntry>Algorithm 2, num stars(4)</AlgorithmEntry>
                    <AlgorithmEntry>Algorithm 3, num stars(3)</AlgorithmEntry>
                    <AlgorithmEntry>Algorithm 4, num stars(7)</AlgorithmEntry>
                </DialogContent>
            </BootstrapDialog>
        </div>
    );
}