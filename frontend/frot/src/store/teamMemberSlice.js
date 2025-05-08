import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  teamRequestIdx: null,
};

const teamMemberSlice = createSlice({
  name: "teamMember",
  initialState,
  reducers: {
    setTeamRequestIdx: (state, action) => {
      state.teamRequestIdx = action.payload;
    },
    clearTeamRequestIdx: (state) => {
      state.teamRequestIdx = null;
    }
  }
});

export const { setTeamRequestIdx, clearTeamRequestIdx } = teamMemberSlice.actions;
export default teamMemberSlice.reducer;