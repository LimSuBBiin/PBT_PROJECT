import * as React from "react";
import { Button, TextField, Grid, Box, Typography, Container, Radio, RadioGroup, FormControlLabel, FormControl, FormLabel, ToggleButton, ToggleButtonGroup } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react"; // useEffect를 import
import { useAuth } from "../AuthContext";

export default function MyPage({setActiveTab}) {
  const [userData, setUserData] = React.useState({
    memAct: "",
    memPwd: "",
    memNme: "",
    phonHed: "",
    phonBod: "",
    phonTal: "",
    emalHed: "",
    emalTal: "",
    memGen: "",
    memWeight: "",
    memHeight: "",
    mainPst: "",
    subPst: "",
    memHist: ""
  });
  const { user, handleLogout } = useAuth();
  const navigate = useNavigate();
  const [token, setToken] = useState(null);
  const [memMstIdx, setMemMstIdx] = useState(null);
  
  useEffect(() => {
    const storedToken = localStorage.getItem("accessToken");
    const storedMemMstIdx = localStorage.getItem("memMstIdx");

    console.log("로그인정보 가져오기", storedMemMstIdx, storedToken);

    if (storedToken && storedMemMstIdx) {
      setToken(storedToken);
      setMemMstIdx(Number(storedMemMstIdx));
    } else {
      console.error("mypage 로그인 정보가 없습니다.");
      navigate("/login");
    }
  }, []);

  useEffect(() => {
    if (user) {
      setUserData({
        memAct: user.memAct || "",
        memPwd: user.memPwd || "",
        memNme: user.memNme || "",
        phonHed: user.phonHed || "",
        phonBod: user.phonBod || "",
        phonTal: user.phonTal || "",
        emalHed: user.emalHed || "",
        emalTal: user.emalTal || "",
        memGen: user.memGen || "",
        memHeight: user.memHeight || "",
        memWeight: user.memWeight || "",
        mainPst: user.mainPst || "",
        subPst: user.subPst || "",
        memHist: user.memHist || ""
      });
    }
  }, [user]);

  const fetchData = async () => {
    if (!memMstIdx || !token) return;
    try {
      const response = await fetch(`http://localhost:8081/profile/mypage?memMstIdx=${memMstIdx}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error('Error fetching user data: ' + response.statusText);
      }

      const data = await response.json();
      setUserData(data);
      console.log("데이터:", data);
    } catch (error) {
      console.error("데이터를 불러오는 데 오류가 발생했습니다.", error);
      alert("세션이 만료되었거나 로그인 정보가 없습니다. 다시 로그인해주세요.");
      navigate("/login");
    }
  };

  useEffect(() => {
    if (token && memMstIdx) {
      fetchData();
    }
  }, [token, memMstIdx]);

  const handleEditClick = () => {
    // navigate("/edit-my-page");
    setActiveTab(8);
  };

  return (
    <Container component="main" maxWidth="xs">
      <Typography variant="h5" fontWeight="bold" color="#E53935" sx={{fontSize: 40, padding : 5}}>마이페이지</Typography>
      <Typography variant="h6">농구에 대한 열정이 가득한만큼</Typography>
      <Typography variant="h6" marginBottom={5}>자세히 적어주세요!</Typography>
      <Box sx={{ marginTop: 0 }}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <TextField label="아이디" value={userData.memAct} fullWidth disabled />
          </Grid>

          <Grid item xs={12}>
            <TextField label="이름" value={userData.memNme} fullWidth disabled />
          </Grid>

          <Grid item xs={12}>
            <FormControl component="fieldset">
              <FormLabel component="legend">성별</FormLabel>
              <RadioGroup row value={userData.memGen} name="memGen">
              <FormControlLabel value="M" control={<Radio/>} label="남성" disabled/>
              <FormControlLabel value="W" control={<Radio/>} label="여성" disabled/>
              </RadioGroup>
            </FormControl>
          </Grid>

          {/* 이메일 */}
          <Grid item xs={5}>
            <TextField label="이메일" value={userData.emalHed} fullWidth disabled />
          </Grid>
          <Grid item xs={1} sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
            <Typography variant="h6">@</Typography>
          </Grid>
          <Grid item xs={6}>
            <TextField label="도메인" value={userData.emalTal} fullWidth disabled />
          </Grid>

          {/* 전화번호 */}
          <Grid item xs={3}>
            <TextField label="전화번호" value={userData.phonHed} fullWidth disabled />
          </Grid>
          <Grid item xs={1} sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
            <Typography variant="h6">-</Typography>
          </Grid>
          <Grid item xs={4}>
            <TextField value={userData.phonBod} fullWidth disabled />
          </Grid>
          <Grid item xs={1} sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
            <Typography variant="h6">-</Typography>
          </Grid>
          <Grid item xs={3}>
            <TextField value={userData.phonTal} fullWidth disabled />
          </Grid>
          <Grid item xs={12}>
            <TextField label="키" value={userData.memHeight} fullWidth disabled />
          </Grid>

          <Grid item xs={12}>
            <TextField label="몸무게(필수아님)" value={userData.memWeight} fullWidth disabled />
          </Grid>

          <Grid item xs={12}>
            <TextField label="주 포지션" value={userData.mainPst} fullWidth disabled />
          </Grid>

          <Grid item xs={12}>
            <TextField label="서브 포지션" value={userData.subPst} fullWidth disabled />
          </Grid>

          <Grid item xs={12}>
            <TextField label="농구 경력" value={userData.memHist} fullWidth disabled/>
          </Grid>

          <Grid item xs={12}>
            <Button variant="contained" onClick={handleEditClick} sx={{ mt: 2 }}>
              수정하기
            </Button>
          </Grid>

          {/* 로그아웃 버튼 */}
          <Grid item xs={12}>
            <Button variant="contained" onClick={handleLogout} sx={{ mt: 2, backgroundColor: "red" }}>
              로그아웃
            </Button>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
}
