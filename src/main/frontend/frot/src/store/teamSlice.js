import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  teamIdx: null,
};

const teamSlice = createSlice({
  name: "team",
  initialState,
  reducers: {
    setTeamIdx: (state, action) => {
      state.teamIdx = action.payload;
    },
    clearTeamIdx: (state) => {
      state.teamIdx = null;
    }
  }
});

export const { setTeamIdx, clearTeamIdx } = teamSlice.actions;
export default teamSlice.reducer;