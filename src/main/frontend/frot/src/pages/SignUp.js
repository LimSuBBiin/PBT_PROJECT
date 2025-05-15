import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";

export default function SignUp() {
  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const userData = {
      memAct: data.get("memAct"), // 아이디
      memPwd: data.get("memPwd"), // 비밀번호
    };

    console.log("회원가입 요청 데이터:", userData);

    // 백엔드 API 호출 (회원가입 요청)
    try {
      const response = await fetch("http://localhost:8081/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        console.log("회원가입 성공!");
        alert("회원가입이 완료되었습니다!");
      } else {
        console.error("회원가입 실패");
        alert("회원가입 실패! 다시 시도해주세요.");
      }
    } catch (error) {
      console.error("오류 발생:", error);
      alert("서버 오류! 나중에 다시 시도해주세요.");
    }
  };

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
          회원가입
        </Typography>
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            {/* 아이디 입력 */}
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                id="memAct"
                label="아이디"
                name="memAct"
                autoComplete="username"
              />
            </Grid>
            {/* 비밀번호 입력 */}
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="memPwd"
                label="비밀번호"
                type="password"
                id="memPwd"
                autoComplete="new-password"
              />
            </Grid>
            {/* 이메일 수신 동의 체크박스 */}
            <Grid item xs={12}>
              <FormControlLabel
                control={<Checkbox value="allowExtraEmails" color="primary" />}
                label="이메일 수신 동의"
              />
            </Grid>
          </Grid>
          {/* 회원가입 버튼 */}
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            회원가입
          </Button>
          {/* 로그인 링크 */}
          <Grid container justifyContent="flex-end">
            <Grid item>
              <Link href="#" variant="body2">
                이미 계정이 있으신가요? 로그인
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Box>
    </Container>
  );
}