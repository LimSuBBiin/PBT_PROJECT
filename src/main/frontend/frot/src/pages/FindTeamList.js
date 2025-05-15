import { useEffect, useState } from "react";
import { Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography, Button } from "@mui/material";
import '@fontsource/bebas-neue'; // or any other
import slamdunkImage from '../images/slamdunk3.jpeg';
const FindTeamList = () => {
    const [teams, setTeams] = useState([]);
    const token = localStorage.getItem("token");
    const memMstIdx = Number(localStorage.getItem("memMstIdx"));
    console.log("userId값과 토큰값 확인",token,memMstIdx);
     
    
const fetchTeams = async () => {
    try {
        const response = await fetch(`http://localhost:8081/team/recommand/list`,{
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        });

        if(!response.ok) {
            throw new Error(`HTTP 오류 발생: ${response.status}`);
        }

        const data = await response.json();
        setTeams(data);
    } catch(error) {
        console.error("팀 목록을 불러오는 중 오류 발생:", error);
    } 
};

const requestJoin = async (teamIdx) => {
  try {
    const response = await fetch(`http://localhost:8081/team/recommand/list/join`,{
      method: "POST",
      headers: {
        "Content-type" : "application/json",
         Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        teamIdx: teamIdx,
        memMstIdx: memMstIdx
      }),
    });

    if(!response.ok) {
      throw new Error("Error fetching user data" + response.statusText);
    }

    const data = await response.json();
    console.log("신청 완료", data);

  } catch (error) {
    console.log("팀 신청 중 오류 발생",error);

  }
};


    useEffect(() => {
        fetchTeams();
    }, []);

    // useEffect(() => {
    //    const response = async fetch(`http://localhost:8081/team/recommand/list`)
    //     .then((response) => response.json )
    //     .then((data) => setTeams(data))
    //     .catch((error) => console.error("Error fetching team list:", error));

    // },[]);


    return (
      <Box sx={{ padding: 0, overflow: "auto", backgroundImage: `url(${slamdunkImage})`,
      backgroundSize: 'cover',
      backgroundRepeat: 'no-repeat',
      backgroundPosition: 'center',
      width: '100vw',
      maxWidth: '100%'
      }}>
          <Typography variant="h5" fontWeight="bold" color="#00008B"   sx={{
    fontFamily: 'Bebas Neue',
    fontSize: '5.5rem',
    letterSpacing: 2,
    color: '#0D47A1'
  }}>
          Find Your Team And JOINING!
        </Typography>
      

   <TableContainer component={Paper} sx={{
    maxWidth: 900,
    margin: "auto",
    mt: 5,
    borderRadius: 3,
    boxShadow: "0 8px 20px rgba(0, 0, 255, 0.1)", // 부드러운 입체감
    backgroundImage: "linear-gradient(to bottom right, #e3f2fd, #ffffff)" // 디지털 느낌
  }}>
  <Table
    sx={{
      border: "2px solid #90CAF9", // 외곽선 파란색
      borderCollapse: "collapse"
    }}
  >
    <TableHead>
      <TableRow sx={{ backgroundColor: "#ADD8E6" }}>
        <TableCell sx={{  fontWeight: "bold", border: "2px solid #1976d2" }}>팀 이름</TableCell>
        <TableCell sx={{ fontWeight: "bold", border: "2px solid #1976d2" }}>팀 대표</TableCell>
        <TableCell sx={{ fontWeight: "bold", border: "2px solid #1976d2" }}>팀 소개</TableCell>
        <TableCell sx={{ fontWeight: "bold", border: "2px solid #1976d2" }} align="center">팀 인원 수</TableCell>
        <TableCell sx={{ fontWeight: "bold", border: "2px solid #1976d2" }} align="center">신청하기</TableCell>
      </TableRow>
    </TableHead>
    <TableBody>
      {teams.map((team, index) => (
        <TableRow key={team.teamRequestIdx}       sx={{
          backgroundColor: index % 2 === 0 ? "#f0f8ff" : "#ffffff", // 번갈아 색상 적용
          '&:hover': {
            backgroundColor: '#e3f2fd', // 호버 효과
          },
        }}>
          <TableCell sx={{ border: "2px solid #1976d2" }}>{team.teamNme}</TableCell>
          <TableCell sx={{ border: "2px solid #1976d2" }}>{team.teamLeaderNme}</TableCell>
          <TableCell sx={{ border: "2px solid #1976d2" }}>{team.teamDesc}</TableCell>
          <TableCell sx={{ border: "2px solid #1976d2" }} align="center">{team.teamMemberCount}</TableCell>
          <TableCell sx={{ border: "2px solid #1976d2" }} align="center">
            <Button variant="contained" color="primary"  onClick={() => requestJoin(team.teamIdx)}>
              신청
            </Button>
          </TableCell>
        </TableRow>
      ))}
    </TableBody>
  </Table>
</TableContainer>


        </Box>
      );
    };
export default FindTeamList;    