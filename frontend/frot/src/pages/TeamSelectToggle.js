import React from "react";
import { FormControl, InputLabel, Select, MenuItem, CircularProgress } from "@mui/material";

export default function TeamSelectToggle({ teamList, selectedTeamIdx, onChange, isLoading }) {

  if (isLoading) {
    return <CircularProgress />; // 로딩 중 표시
  }
  return (
    
    <FormControl size="small" sx={{ width: 300 }} disabled={isLoading || teamList.length === 0}>
      <InputLabel id="team-select-label">팀 선택</InputLabel>
      <Select
        labelId="team-select-label"
        id="team-select"
        value={selectedTeamIdx || ""}
        label="팀 선택"
        onChange={(e) => onChange(e.target.value)}
      >
 {teamList.map((team) => (
          <MenuItem key={team.teamIdx} value={team.teamIdx}>
            {team.teamNme} {team.roleType}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}