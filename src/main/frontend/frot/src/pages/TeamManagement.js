import React, { useEffect, useState } from "react";
import { Box, Typography, Table, TableHead, TableBody, TableRow, TableCell,  Card, Button, CardContent, TableContainer, Paper} from '@mui/material';
import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import SportsBasketballIcon from "@mui/icons-material/SportsBasketball";
import BlurredTableContainer from "./BlurredTableContainer";
import {useAuth} from "../AuthContext";
import { setTeamIdx, clearTeamIdx } from "../store/teamSlice";
// import { Table, TableHeader, TableRow, TableHead, TableBody, TableCell } from "@/components/ui/table";
const TeamManagement = () => {
  const dispatch = useDispatch();

  const teamIdx = useSelector((state) => state.team.teamIdx);
  // const [teamIdx, setTeamIdx] = useState(null);
  const [members, setMembers] = useState([]);
  const [pendingMembers, setPendingMembers] = useState([]);
  const [maxMembers, setMaxMembers] = useState(0);
  const token = localStorage.getItem("accessToken");
  const memMstIdx = Number(localStorage.getItem("memMstIdx"));
  console.log("반응확인++", token,memMstIdx);
  console.log("현재 teamIDx확인:",teamIdx);



  /** 이제 팀 선택을 홈페이지 상단에서 선택할 수 있으므로 teamIdx는 필요가 없음. */
  // 팀 인덱스를 가져오는 useEffect
  // useEffect(() => {
  //   const fetchTeamIdx = async () => {
  //     try {
  //       const response = await fetch(`http://localhost:8081/team/find?memMstIdx=${memMstIdx}`, {
  //         method: "GET",
  //         headers: {
  //           "Content-Type": "application/json",
  //           Authorization: `Bearer ${token}`,
  //         }
  //       });

  //       if (!response.ok) {
  //         throw new Error("Error fetching team idx: " + response.statusText);
  //       }

  //       const data = await response.json();
  //       dispatch(setTeamIdx(data));
  //       console.log("팀 idx가져오기 성공", data);
  //     } catch (error) {
  //       console.error("Error fetching team idx:", error);
  //     }
  //   };

  //   if (memMstIdx) {
  //     fetchTeamIdx();
  //   }
  // }, [memMstIdx, token, dispatch]);



console.log(teamIdx);  

    // teamIdx가 변경될 때 fetchTeamData 실행
useEffect(() => {
  if (teamIdx) {
    console.log("변경된 teamIdx 확인:", teamIdx);
    console.log("토큰확인",token);
    fetchTeamData(teamIdx);
  }
}, [teamIdx]); // teamIdx 변경될 때 실행
  

    const fetchTeamData = async (updatedTeamIdx) => {
      if (!updatedTeamIdx) {
      console.log("fetchTeamData id값 확인: ", updatedTeamIdx);
        return; // teamIdx가 없으면 요청을 보내지 않음
      }
      try {
        // 팀원 목록
        const membersResponse = await fetch(`http://localhost:8081/team/leader/myteam/members?teamIdx=${updatedTeamIdx}&memMstIdx=${memMstIdx}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          }
        });

        if (!membersResponse.ok) {
          throw new Error("Error fetching members: " + membersResponse.statusText);
        }

        const membersData = await membersResponse.json();
        setMembers(membersData);
        if (membersData.length > 0) {
          setMaxMembers(membersData[0].maxMembers);
        }

        // 승인 대기 목록
        const pendingResponse = await fetch(`http://localhost:8081/team/leader/myteam/execute?teamIdx=${updatedTeamIdx}&memMstIdx=${memMstIdx}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          }
        });

        if (!pendingResponse.ok) {
          throw new Error("Error fetching pending members: " + pendingResponse.statusText);
        }

        const pendingData = await pendingResponse.json();
        setPendingMembers(pendingData);

      } catch (error) {
        console.error("Error fetching team data:", error);
      }

    };

    // fetchTeamData();
  
    
  




  const handleApproval = async (teamRequestIdx, actionType) => {

    const url = actionType === "approve"
    ? `http://localhost:8081/team/approve?teamLeaderIdx=${memMstIdx}&teamRequestIdx=${teamRequestIdx}&teamIdx=${teamIdx}`
    : `http://localhost:8081/team/reject?teamLeaderIdx=${memMstIdx}&teamRequestIdx=${teamRequestIdx}&teamIdx=${teamIdx}`;

    try{
      const response = await fetch(url, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`Error processing request: ${response.statusText}`);
      }

      // 승인/거절 성공 시 대기 목록에서 제거
    setPendingMembers(pendingMembers.filter((m) => m.teamRequestIdx !== teamRequestIdx));


    } catch (error) {
      console.error("Error processing request:", error);
    }
  }
  

  

  return (
    <div className="p-4 space-y-6">
<Box
  sx={{
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    bgcolor: '#f0f4ff',
    border: '2px solid #1976d2',
    borderRadius: 2,
    p: 2,
    maxWidth: 1000,
    margin: 'auto',
    boxShadow: 3,
  }}
>
  <Box sx={{ display: 'flex', alignItems: 'center' }}>
    <SportsBasketballIcon sx={{ color: '#1976d2', fontSize: 30, mr: 1 }} />
    <Typography variant="h6" fontWeight="bold" color="#1976d2">
      팀 관리
    </Typography>
  </Box>
  
  <Typography color="text.secondary" sx={{ fontSize: '0.95rem' }}>
    총 팀원 수: <strong>{maxMembers}</strong>
  </Typography>
</Box>
      
      <BlurredTableContainer componont={Paper}>
      <Table>
        <TableHead>
          <TableRow sx={{ backgroundColor: "#f5f5f5" }}>
            <TableCell sx={{ fontWeight: "bold" }}>이름</TableCell>
            <TableCell sx={{ fontWeight: "bold" }}>역할</TableCell>
            <TableCell sx={{ fontWeight: "bold" }}>성별</TableCell>
            <TableCell sx={{ fontWeight: "bold" }}>키</TableCell>
            <TableCell sx={{ fontWeight: "bold" }}>몸무게</TableCell>
            <TableCell sx={{ fontWeight: "bold" }}>포지션</TableCell>
            <TableCell sx={{ fontWeight: "bold" }}>경력</TableCell>
            <TableCell sx={{ fontWeight: "bold" }}>전화번호</TableCell>
            <TableCell sx={{ fontWeight: "bold" }}>이메일</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {members.map((member) => (
            <TableRow key={member.memMstIdx} hover>
              <TableCell>{member.memNme}</TableCell>
              <TableCell>{member.roleType}</TableCell>
              <TableCell>{member.memGen}</TableCell>
              <TableCell>{member.memHeight}</TableCell>
              <TableCell>{member.memWeight}</TableCell>
              <TableCell>{`${member.mainPst}(${member.subPst})`}</TableCell>
              <TableCell>{member.memHist}</TableCell>
              <TableCell>{`${member.phonHed}-${member.phonBod}-${member.phonTal}`}</TableCell>
              <TableCell>{`${member.emalHed}@${member.emalTal}`}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </BlurredTableContainer>

 <Box
  sx={{
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    bgcolor: '#f0f4ff',
    border: '2px solid #1976d2',
    borderRadius: 2,
    p: 2,
    maxWidth: 1000,
    margin: 'auto',
    mt: 2,
    boxShadow: 3,
  }}
>
    <Typography variant="h6" fontWeight="bold" color="#1976d2" align= 'center'>
      팀 신청 관리
    </Typography>
      </Box>
       <TableContainer sx={{
        maxWidth: 900,
        margin: "auto"
       }}>
      <Table>
        <TableHead>
          <TableRow sx={{ backgroundColor: "#f5f5f5" }}>
          <TableCell sx={{ fontWeight: "bold" }}>이름</TableCell>
      <TableCell sx={{ fontWeight: "bold" }}>전화번호</TableCell>
      <TableCell sx={{ fontWeight: "bold" }}>이메일</TableCell>
      <TableCell sx={{fontWeight: "bold"}}> 승인여부</TableCell>  
          </TableRow>
        </TableHead>
        <TableBody>
          {pendingMembers.map((member) => (
            <TableRow key = {member.memMstIdx} hover>
              <TableCell>{member.memNme}</TableCell>
              <TableCell>{`${member.phonHed}-${member.phonBod}-${member.phonTal}`}</TableCell>
              <TableCell>{`${member.emalHed}@${member.emalTal}`}</TableCell>
              <TableCell>
              <Button onClick={() => handleApproval(member.teamRequestIdx, "approve")}>승인</Button>
              <Button onClick={() => handleApproval(member.teamRequestIdx, "reject")} sx={{ ml: 1 }}>거부</Button>
                </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>

    </div>
  );
};

export default TeamManagement;
