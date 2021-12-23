
import { createSlice } from '@reduxjs/toolkit'

export const ModelSlice = createSlice({
  name: 'model',
  initialState: {
    currentUser: null,
    ontologyHierarchy: null
  },
  reducers: {
    updateUser: (state, action) => {
      state.currentUser = action.payload
    },
    updateOntology: (state, action) => {
        state.ontologyHierarchy = action.payload
    }
  }
})


export const { updateUser, updateOntology } = ModelSlice.actions
export default ModelSlice.reducer