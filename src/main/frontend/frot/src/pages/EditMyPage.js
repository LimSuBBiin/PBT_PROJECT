import * as React from "react";
import { Button, TextField, Grid, Box, Typography, Container,  FormControl, FormLabel, RadioGroup, FormControlLabel, Radio, ToggleButton, ToggleButtonGroup } from "@mui/material";
import { useNavigate } from "react-router-dom";

export default function EditMyPage({setActiveTab}) {
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

  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const memMstIdx = Number(localStorage.getItem("memMstIdx"));
  // console.log("수정페이지 토큰값 확인:",token);
  const handleChange = (e) => {
    setUserData({
      ...userData,
      [e.target.name]: e.target.value,
    });
  };




    // Fetch user data from backend GET
      const fetchData = async () => {
        if (!memMstIdx) {
          console.error("memSeq 값이 존재하지 않습니다.");
          return;
        }
    
        try {
          const response = await fetch(`http://localhost:8081/profile/edit-my-page?memMstIdx=${memMstIdx}`, {
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
          setUserData(data);  // 받아온 데이터를 userData 상태에 저장합니다.
        } catch (error) {
          console.error("데이터를 불러오는 데 오류가 발생했습니다.", error);
          alert("세션이 만료되었거나 로그인 정보가 없습니다. 다시 로그인해주세요.");
        }
      };
    
    
        // useEffect로 컴포넌트가 마운트될 때 fetchData 호출
        React.useEffect(() => {
          fetchData();
        }, [memMstIdx, token]);  // memMstIdx와 token이 변경될 때마다 호출
    
    







  const handleSubmit = async (event) => {
    event.preventDefault();

    const updatedData = {
      memMstIdx: memMstIdx,
      memNme: userData.memNme,
      phonHed: userData.phonHed,
      phonBod: userData.phonBod,
      phonTal: userData.phonTal,
      emalHed: userData.emalHed,
      emalTal: userData.emalTal,
      memGen: userData.memGen,
      memHeight: userData.memHeight,
      memWeight: userData.memWeight,
      mainPst: userData.mainPst,
      subPst: userData.subPst,
      memHist: userData.memHist
    };

    console.log("수정된 데이터:", updatedData);

    try {
      const response = await fetch(`http://localhost:8081/profile/edit-my-page`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(updatedData),
      });

      if (response.ok) {
        console.log("수정 완료!");
        alert("수정이 완료되었습니다!");
        setActiveTab(0);
      } else {
        console.error("수정 실패");
        alert("수정 실패! 다시 시도해주세요.");
      }
    } catch (error) {
      console.error("오류 발생:", error);
      alert("서버 오류! 나중에 다시 시도해주세요.");
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <Box sx={{ marginTop: 8 }}>
        <Typography variant="h5">내 정보 수정</Typography>
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <Grid container spacing={2}>

            <Grid item xs={12}>
              <TextField label="아이디" value={userData.memAct} fullWidth disabled />
            </Grid>




            <Grid item xs={12}>
              <TextField label="이름" name="memNme" value={userData.memNme} fullWidth onChange={handleChange} />
            </Grid>

            <Grid item xs={12}>
            <FormControl component="fieldset">
              <FormLabel component="legend">성별</FormLabel>
              <RadioGroup row value={userData.memGen} name="memGen" onChange={handleChange}>
              <FormControlLabel value="M" control={<Radio/>} label="남성" />
              <FormControlLabel value="W" control={<Radio/>} label="여성" />
              </RadioGroup>
            </FormControl>
          </Grid>

            

            {/* 이메일 */}
            <Grid item xs={5}>
              <TextField label="이메일" name="emalHed" value={userData.emalHed} fullWidth onChange={handleChange} />
            </Grid>
            <Grid item xs={1} sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
              <Typography variant="h6">@</Typography>
            </Grid>
            <Grid item xs={6}>
              <TextField label="도메인" name="emalTal" value={userData.emalTal} fullWidth onChange={handleChange} />
            </Grid>

            {/* 전화번호 */}
            <Grid item xs={3}>
              <TextField label="전화번호" name="phonHed" value={userData.phonHed} fullWidth onChange={handleChange} />
            </Grid>
            <Grid item xs={1} sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
              <Typography variant="h6">-</Typography>
            </Grid>
            <Grid item xs={4}>
              <TextField name="phonBod" value={userData.phonBod} fullWidth onChange={handleChange} />
            </Grid>
            <Grid item xs={1} sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
              <Typography variant="h6">-</Typography>
            </Grid>
            <Grid item xs={3}>
              <TextField name="phonTal" value={userData.phonTal} fullWidth onChange={handleChange} />
            </Grid>

            <Grid item xs={4}>
              <TextField name = "memHeight" label="키" value={userData.memHeight} fullWidth onChange={handleChange} />
            </Grid>

            <Grid item xs={2} sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
              <Typography variant="h6">cm</Typography>
            </Grid>

            <Grid item xs={4}>
              <TextField name = "memWeight" label="몸무게" value={userData.memWeight} fullWidth onChange={handleChange} />
            </Grid>

            <Grid item xs={2} sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
              <Typography variant="h6">kg</Typography>
            </Grid>

            <Grid item xs={12}>
            <Typography variant="h6">주 포지션</Typography>
            <ToggleButtonGroup 
                  name = "mainPst"
                  value={userData.mainPst}
                  exclusive
                  onChange={(e, newValue) => {
                    setUserData({ ...userData, mainPst: newValue });
                  }}
                  fullWidth
                >
                {["PG", "SG", "SF", "PF", "C"].map((pos) => (
            <ToggleButton key={pos} value={pos} sx={{ flex: 1 }}>
              {pos}
            </ToggleButton>
          ))}

            </ToggleButtonGroup>
            </Grid>
          

            <Grid item xs={12}>
            <Typography variant="h6">서브 포지션</Typography>
            <ToggleButtonGroup
                  name = "subPst"
                  value={userData.subPst}
                  exclusive
                  onChange={(e, newValue) => {
                    setUserData({ ...userData, subPst: newValue });
                  }}
                  fullWidth
                >
                {["PG", "SG", "SF", "PF", "C"].map((pos) => (
            <ToggleButton key={pos} value={pos} sx={{ flex: 1 }}>
              {pos}
            </ToggleButton>
          ))}

            </ToggleButtonGroup>
            </Grid>
          
            <Grid item xs={12}>
              
            <Grid item xs={4}>
              <TextField name = "memHist" label="농구 경력" value={userData.memHist} fullWidth onChange={handleChange} />
            </Grid>
            </Grid>

            {/* 수정 완료 버튼 */}
            <Grid item xs={12}>
              <Button type="submit" fullWidth variant="contained" sx={{ mt: 3 }}>
                수정 완료
              </Button>
            </Grid>

          </Grid>
        </Box>
      </Box>
    </Container>
  );
}
