import React, { useState } from "react";
import { TextField, Button, Card, CardContent, Typography, Container, Box } from "@mui/material";
import { useNavigate } from "react-router-dom";

export default function CreateTeam({setActiveTab, refetchTeams}) {
  const [teamData, setTeamData] = useState({
    teamNme: "",
    teamDesc: "",
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const navigate = useNavigate();
  const token = localStorage.getItem("accessToken");
  const memMstIdx = Number(localStorage.getItem("memMstIdx"));
  console.log(memMstIdx);
  console.log("마이페이지 토큰값확인", token);
  
  const handleChange = (e) => {
    setTeamData({
      ...teamData,
      [e.target.name]: e.target.value,
    });
  };

  const handleCreateTeam = async (event) => {
    event.preventDefault(); // 폼 제출 시 기본 동작을 막기 위해 추가
  
    if (!memMstIdx) {
      console.error("memSeq 값이 존재하지 않습니다.");
      return;
    }
  
    if (!teamData.teamNme) {
      alert("팀 이름을 입력하세요.");
      return;
    }
  
    if (!teamData.teamDesc) {
      alert("팀 설명을 입력하세요.");
      return;
    }
  
    setLoading(true);
  
    try {
      const response = await fetch(`http://localhost:8081/team/create`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          teamLeaderIdx: memMstIdx, // memMstIdx를 teamLeaderIdx로 변경
          teamNme: teamData.teamNme,
          teamDesc: teamData.teamDesc,
        }),
      });
  
      if (!response.ok) {
        throw new Error("Error fetching user data: " + response.statusText);
      }
  
      const data = await response.json();
      console.log("서버 응답", data);
      alert("팀 생성이 완료되었습니다!");
      
      if(refetchTeams) {
        await refetchTeams();
      }

      setMessage(""); // 메시지 초기화
      setActiveTab(1);

      // navigate("/home");
      // window.location.reload();
    } catch (error) {
      console.error("팀 생성 중 오류 발생:", error);
      setMessage("서버 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };
  

  return (
    <Container maxWidth="sm">
      <Card sx={{ mt: 5, p: 3 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>
            농구팀 생성
          </Typography>

          <form onSubmit={handleCreateTeam}> {/* onSubmit으로 이벤트 연결 */}
            <TextField
              label="팀 이름"
              fullWidth
              margin="normal"
              name="teamNme"
              value={teamData.teamNme}
              onChange={handleChange}
            />

            <TextField
              label="팀 설명"
              fullWidth
              margin="normal"
              name="teamDesc"
              multiline
              rows={3}
              value={teamData.teamDesc}
              onChange={handleChange}
            />

            <Box mt={2}>
              <Button
                type="submit" // 버튼이 폼을 제출하도록 하기 위해 type="submit"을 추가
                variant="contained"
                color="primary"
                fullWidth
                disabled={loading}
              >
                {loading ? "생성 중..." : "팀 생성"}
              </Button>
            </Box>
          </form>

          {message && (
            <Typography variant="body2" color="error" mt={2}>
              {message}
            </Typography>
          )}
        </CardContent>
      </Card>
    </Container>
  );
}
