
import { createSlice } from '@reduxjs/toolkit'
import { expandParents } from '../boundary/common/Common';
export const ViewModelSlice = createSlice({
  name: 'view_model',
  initialState: {
    selectedOntologyItem: null,
    selectedOntologyItemParent: null,
    selectedOntologyItemType: "",
    selectedItem: {},
    cachedSets: {},
    expandedOntologyItems: {},
    openPanels: [],
    operationStatus: {},
    notificationQueue: [],
    loadingStatus: {},
    headerTitle: "Welcome to AlgoHub",
    removeRequest: {
      state: "complete"
    }
  },
  reducers: {
    togglePanelVisibility: (state, action) => {

        if(!(action.payload.name in state.openPanels)) {
            state.openPanels.push(action.payload.name);
        } else {
            state.openPanels = state.openPanels.filter((item) => item != action.payload.name);
        }
    },
    setPanelVisibility: (state, action) => {
      console.log(action)
        state.openPanels = state.openPanels.filter((item) => item != action.payload.name);

        if(action.payload.state) {
            state.openPanels.push(action.payload.name);
        }
    },
    updateExpandedOntologyItems: (state, action) => {

        state.expandedOntologyItems = action.payload;
    },
    toggleOntologyItem: (state, action) => {

      state.expandedOntologyItems[action.payload] = !state.expandedOntologyItems[action.payload];

    },
    updateSelectedOntologyItem: (state, action) => {

      if(!action.payload) {
        state.headerTitle = "Welcome to AlgoHub";
        state.selectedOntologyItemType = "";
        state.selectedOntologyItem = null;

        return;

      }

      var selected = action.payload.item;
      var ontologyHierarchy = action.payload.ontology;

      var title = "";
      if(selected) {

        var parentCandidates = ontologyHierarchy.filter((item) => item.id == selected.parentId);
        if(parentCandidates.length > 0) {

          var parent = parentCandidates[0];
    
          if(parent) {
            title = parent.name;
          }

        }

        title += "." + selected.name;
  
        state.selectedOntologyItemType = selected.typeName;
        state.headerTitle = title;
      }


      state.selectedOntologyItem = selected;
    },
    updateExpanded: (state, action) => {

      state.expandedOntologyItems = action.payload;

    }, 
    updateNotificationQueue: (state, action) => {

      state.notificationQueue = action.payload;

    },
    enqueueNotification: (state, action) => {

      state.notificationQueue.push(action.payload);
    },
    updateNotificationStatus: (state, action) => {

      var notification = state.notificationQueue.find((item) => item.name == action.payload.name);

      if(notification) {
          notification.status = action.payload.status;
      }

    },
    updateSelectedItem: (state, action) => {

      if(!action.payload.item) {

        state.selectedItem[action.payload.name] = null;
      }

      state.selectedItem[action.payload.name] = action.payload.item;

    },
    updateLoadingStatus: (state, action) => {

      state.loadingStatus[action.payload.name] = action.payload.state;
    },
    updateRemoveRequest :(state, action) => {

      state.removeRequest = action.payload;
    },
    updateCachedSet: (state, action) => {

      if(!(action.payload.name in Object.keys(state.cachedSets))) 
        state.cachedSets[action.payload.name] = []

      state.cachedSets[action.payload.name] = action.payload.state;
    }
  }
})


export const { 
                togglePanelVisibility, 
                setPanelVisibility,
                updateExpandedOntologyItems,
                updateExpanded,
                updateSelectedItem,
                updateSelectedOntologyItem,
                updateLoadingStatus,
                toggleOntologyItem,
                updateNotificationQueue,
                updateNotificationStatus,
                enqueueNotification,
                updateRemoveRequest,
                updateCachedSet
} = ViewModelSlice.actions
export default ViewModelSlice.reducer