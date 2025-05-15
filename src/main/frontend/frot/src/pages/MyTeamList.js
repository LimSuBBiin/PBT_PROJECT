import { useEffect, useState } from "react";
import { Card, CardContent, Typography, CircularProgress, Container, Grid } from "@mui/material";
import { useNavigate } from "react-router-dom"; // 추가
import { useAuth } from "../AuthContext";
const MyTeamsList = () => {
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const {user} = useAuth();
  const token = localStorage.getItem("accessToken");
  const memMstIdx = Number(localStorage.getItem("memMstIdx"));
  console.log("userid값과 토큰값 확인", memMstIdx, token);

  const fetchTeams = async () => {
    if (!memMstIdx) {
      console.error("memMstIdx 값이 존재하지 않습니다.");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8081/team/myteam/list?memMstIdx=${memMstIdx}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP 오류 발생: ${response.status}`);
      }

      const data = await response.json();
      setTeams(data);
    } catch (error) {
      console.error("팀 목록을 불러오는 중 오류 발생:", error);
    } finally {
      setLoading(false);
    }
  };

  // 🔹 첫 번째 useEffect: 페이지 로드 시 팀 목록 불러오기
  useEffect(() => {
    fetchTeams();
  }, []);

  // 🔹 두 번째 useEffect: 로그인 정보가 없으면 로그인 페이지로 이동
  useEffect(() => {
    if (!token || !memMstIdx) {
      console.error("로그인 정보가 없습니다.");
      navigate("/login");
    } else {
      fetchTeams(); // 여기서 fetchTeams 사용 가능
    }
  }, [token, memMstIdx]); // 🔹 의존성 배열 수정

  if (loading) return <CircularProgress />;

  return (
    <Container>
      <Typography variant="h4" gutterBottom>내 팀 목록</Typography>
      <Grid container spacing={2}>
        {teams.length > 0 ? (
          teams.map((team) => (
            <Grid item xs={12} sm={6} md={4} key={team.teamRequestIdx}>
              <Card>
                <CardContent>
                  <Typography variant="h6">{team.teamNme}</Typography>
                  <Typography variant="body2" color="textSecondary">{team.teamDesc}</Typography>
                  <Typography variant="subtitle2">리더: {team.teamLeaderNme}</Typography>
                </CardContent>
              </Card>
            </Grid>
          ))
        ) : (
          <Typography variant="body1">가입된 팀이 없습니다.</Typography>
        )}
      </Grid>
    </Container>
  );
};

export default MyTeamsList;
