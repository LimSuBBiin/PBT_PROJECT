import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Link from "@mui/material/Link";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../AuthContext"
export default function SignIn() {
  const navigate = useNavigate();
  const { fetchUser } = useAuth(); // AuthContext에서 fetchUser 가져오기
  const [loginData, setLoginData] = React.useState({
    memAct: "",
    memPwd: "",
  });
  const [accessToken, setAccessToken] = React.useState(null);
  const [memMstIdx, setMemMstIdx] = React.useState(null);

  const handleChange = (e) => {
    setLoginData({
      ...loginData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!loginData.memAct || !loginData.memPwd) {
        alert("아이디와 비밀번호를 입력해주세요.");
        return;
    }

    try {
        const response = await fetch("http://localhost:8081/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include", // 쿠키를 사용하도록 설정
            body: JSON.stringify(loginData),
        });

        if (response.ok) {
            const data = await response.json();
            const accessToken = data.accessToken; // 서버 응답에서 토큰 가져오기
            localStorage.setItem('accessToken', accessToken); // localStorage에 저장
            console.log("로그인 성공!");
            
            await fetchUser(accessToken);
            
            navigate("/Home");
            // const token = localStorage.getItem('accessToken');
            // console.log("토큰값 가져오기:",token);
            // console.log("회원 IDX값:", memMstIdx);
            // // 로그인 성공 후 내 정보 가져오기
            // const userResponse = await fetch("http://localhost:8081/auth/me", {
            //     method: "GET",
            //     headers: {'Authorization': `Bearer ${token}`}, 
            //     credentials: "include", // 쿠키를 포함해서 요청
            // });

            // if (userResponse.ok) {
            //     const userData = await userResponse.json();
            //     console.log("유저 데이터:", userData);
                
            //     // 사용자 ID를 상태에 저장
            //     setMemMstIdx(userData.memMstIdx);
            //     // fetchUser();
            //     navigate("/Home");
            // } else {
            //     console.error("사용자 정보 불러오기 실패");
            // }
        } else {
            const errorData = await response.json();
            alert(`로그인 실패! ${errorData.message || '아이디와 비밀번호를 확인해주세요.'}`);
        }
    } catch (error) {
        alert("서버 오류! 나중에 다시 시도해주세요.");
    }
};

// React.useEffect(() => {
//   if (memMstIdx !== null) {
//       console.log("회원 IDX 값:", memMstIdx); // 상태가 변경된 후에 출력
//   }
//   navigate("/Home");
// }, [memMstIdx]); // memMstIdx가 변경될 때마다 실행


  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <Box
        sx={{
          marginTop: 8,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          로그인
        </Typography>
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                id="memAct"
                label="아이디"
                name="memAct"
                value={loginData.memAct}
                onChange={handleChange}
                autoComplete="username"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="memPwd"
                label="비밀번호"
                type="password"
                id="memPwd"
                value={loginData.memPwd}
                onChange={handleChange}
                autoComplete="new-password"
              />
            </Grid>
          </Grid>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            로그인
          </Button>
          <Grid container justifyContent="flex-end">
            <Grid item>
              <Link href="/signup" variant="body2">
                계정이 없으신가요? 회원가입
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Box>
    </Container>
  );
}
