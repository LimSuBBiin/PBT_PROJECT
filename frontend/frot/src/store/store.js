import { configureStore } from "@reduxjs/toolkit";
import teamReducer from "./teamSlice";
import teamMemberReducer from "./teamMemberSlice";

export const store = configureStore({
  reducer: {
    team: teamReducer,
    teamMember: teamMemberReducer
  }
});
