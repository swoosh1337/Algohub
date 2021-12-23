import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import LoadingButton from '@mui/lab/LoadingButton';
import { updateRemoveRequest, updateCachedSet } from '../../model/ViewModel';
import { useSelector, useDispatch } from 'react-redux';

export default function RemoveDialog(props) {

  const [loading, setLoading] = React.useState(false);
  const [requestError, setRequestError] = React.useState("");
  const ontologyHierarchy = useSelector(state => state.model.ontologyHierarchy);
  const dispatch = useDispatch();
  const selectedItem = useSelector(state => state.viewModel.selectedOntologyItem);
  const cachedSets = useSelector(state => state.viewModel.cachedSets);
  
  var endpointAliases = {
    "problem_instance": "problemInstance"
  }

  const handleClose = (err) => {

    dispatch(updateRemoveRequest(
      { ...props.removeRequest,
        msg: "",
        state: "cancelled"
      }
    ))
  };

  const performDelete = (props) => {

    setLoading(true)

    var item = props.removeRequest.item;
    var parent = {typeName: item.typeName};
    console.log("Executing delete: ", item);

    if(item) {

      var candidates = ontologyHierarchy.filter((candidate) => candidate.id == item.parentId)
      if(candidates.length > 0) {
        parent = candidates[0];
      }
    }

    if(item.typeName == "classification" || item.typeName == "algorithm" || item.typeName == "implementation")
      props.ontologyController.selectOntologyItem(parent);

    var objName = props.removeRequest.item.typeName;
    if(Object.keys(endpointAliases).includes(objName)) {

      objName = endpointAliases[objName]
    }

    var endpoint = objName + "s" + "/remove";

    if(props.removeRequest.customEndpoint) {

      endpoint = props.removeRequest.customEndpoint
    }

    var reqData = {
      id: props.removeRequest.item.id
    }

    if(props.removeRequest.customData) {

      reqData = {...props.removeRequest.customData};
    }

    props.requestService.executePostRequest(
      (err, data) => {
          console.log(err)
          setLoading(false)
          
          dispatch(updateRemoveRequest(
            { ...props.removeRequest,
              msg: "",
              customEndpoint: null,
              customData: null,
              state: "complete"
            }
          ))

          if (err.length == 0) {

              var oldCache = cachedSets[props.removeRequest.item.typeName]

              if(oldCache ) {
    
                dispatch(updateCachedSet(
                  { 
                    name: props.removeRequest.item.typeName,
                    state: oldCache.filter((item) => item.id != props.removeRequest.item.id)
                  }
                ))
              }
  
              handleClose();
          }

      },
      reqData,
      endpoint,
      "Failed to remove " + objName,
      "Removed " + objName + " successfully."
  );
    
  }


  return (
    <div>
      <Dialog
        open={props.open}
        //onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">
          {"Remove Confirmation"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            {props.removeRequest.state != "complete" && props.removeRequest.msg}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
        <LoadingButton
              onClick={()=> {performDelete(props)}}
              loading={loading}
              loadingPosition="center"
              variant="contained"
          >
              Yes
          </LoadingButton>
          <Button onClick={handleClose} autoFocus>No</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}