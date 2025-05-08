import React, { useEffect, useState } from "react";
import { Grid, TextField, Box, Typography, Table, TableHead, TableBody, TableRow, TableCell,  Card, Button, CardContent, TableContainer, Paper} from '@mui/material';
import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import {useAuth} from "../AuthContext";
import { setTeamIdx, clearTeamIdx } from "../store/teamSlice";
import { setTeamRequestIdx } from "../store/teamMemberSlice";
import SportsBasketballIcon from "@mui/icons-material/SportsBasketball";
import { blueGrey } from "@mui/material/colors";

// import { Table, TableHeader, TableRow, TableHead, TableBody, TableCell } from "@/components/ui/table";
// const CreateNotice = () => {
//   const dispatch = useDispatch();

export default function CreateNotice({setActiveTab}) {
    const dispatch = useDispatch();
    const [notice, setNotice] = useState({
      memMstIdx:  "",
      noticeGameIdx: "",
      teamRequestIdx: "",
      memNme: "",
      roleType: "",
      noticeTitle: "",
      noticeLocation: "",
      noticeGameDate: "",
      totalCount: "",
      guardCount: "",
      forwardCount: "",
      centerCount: "",
      cost: "",
});
    const teamIdx = useSelector((state) => state.team.teamIdx);
    const teamRequestIdx = useSelector((state) => state.teamMember.teamRequestIdx);
    const token = localStorage.getItem("accessToken");
    const memMstIdx = Number(localStorage.getItem("memMstIdx"));
    console.log("반응확인++(CREATENOTICE)", token,memMstIdx);
    console.log("현재 teamIDx확인(CREATENOTICE):",teamIdx);
    console.log("현재 teamRequestIdx확인:",teamRequestIdx);
  

    useEffect(() => {
        const fetchTeamRequestIdx = async () => {
            try{
                const response = await fetch(`http://localhost:8081/notice/getTeamRequestIdx?teamIdx=${teamIdx}&memMstIdx=${memMstIdx}`, {
                    method: "GET",
                    headers: { Authorization : `Bearer ${token}`},
                    credentials: "include",
                });

                if(!response.ok) {
                    throw new Error("Error fetching team idx:" + response.statusText);
                }

                const data = await response.json();
                dispatch(setTeamRequestIdx(data));
                console.log("팀 멤버 idx가져오기 성공", data);
            } catch (error) {
                console.error("Error fetching teamRequestidx:", error);
            }
        }

        if(memMstIdx&&teamIdx) {
            fetchTeamRequestIdx();
        }

    }, [memMstIdx, teamIdx ,token, dispatch]);

    useEffect(() => {
      if (teamRequestIdx && teamIdx && memMstIdx) {
        setNotice((prev) => ({
          ...prev,
          teamRequestIdx,
          teamIdx,
          memMstIdx
        }));
      }
    }, [teamRequestIdx, teamIdx, memMstIdx]);


    const handleCreateNotice = async (event) => {
        event.preventDefault();

        if (!memMstIdx) {
            console.error("memSeq 값이 존재하지 않습니다.");
            return;
          }
        
        if(!teamIdx) {
            console.error("teamIdx값이 존재하지 않습니다.")
            return;
        }  
        
        try {
            const response = await fetch(`http://localhost:8081/notice/create`, {
                method : "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify(notice),
            });

            if(!response){
              throw new Error("글 생성 실패");
            }

            const result = await response.json();
            console.log("공지 생성 성공", result);
            alert("경기 모집글 생성 완료!");
            setActiveTab(6);
        } catch (error) {
          console.error("공지 생성 중 오류 발생:", error);
        }
        
    };


    return (
      <Box
        sx={{
          minHeight: "100vh",
          background: "linear-gradient(to bottom right, #e3f2fd, #bbdefb)",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          p: 2,
        }}
      >
        <Card sx={{ maxWidth: 800, width: "100%", borderRadius: 4, boxShadow: 6 }}>
          <CardContent>
            <Box sx={{ textAlign: "center", mb: 4 }}>
              <SportsBasketballIcon sx={{ fontSize: 60, color: "#1976d2" }} />
              <Typography variant="h4" fontWeight="bold" color="primary">
                농구팀 경기 공지 등록
              </Typography>
              <Typography variant="body2" color="textSecondary">
                팀원들과 함께할 경기를 계획해보세요
              </Typography>
            </Box>
  
            <form onSubmit={handleCreateNotice}>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="공지 제목"
                    value={notice.noticeTitle}
                    onChange={(e) =>
                      setNotice({ ...notice, noticeTitle: e.target.value })
                    }
                  />
                </Grid>

                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="공지 내용"
                    value={notice.noticeDesc}
                    onChange={(e) =>
                      setNotice({ ...notice, noticeDesc: e.target.value })
                    }
                    multiline
                    rows={4} // 원하는 줄 수로 설정
                  />
                </Grid>

                

                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="경기 장소"
                    value={notice.noticeLocation}
                    onChange={(e) =>
                      setNotice({ ...notice, noticeLocation: e.target.value })
                    }
                  />
                </Grid>
  
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    type="datetime-local"
                    label="경기 날짜"
                    InputLabelProps={{ shrink: true }}
                    value={notice.noticeGameDate}
                    onChange={(e) =>
                      setNotice({ ...notice, noticeGameDate: e.target.value })
                    }
                  />
                </Grid>
                
      
                <Grid item xs={5}>
                  <TextField
                    fullWidth
                    label="대관 비용"
                    value={notice.cost}
                    onChange={(e) =>
                      setNotice({ ...notice, cost: e.target.value })
                    }
                  />
                </Grid>    


                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    type="number"
                    label="모집 인원 총합"
                    value={notice.totalCount}
                    onChange={(e) =>
                      setNotice({ ...notice, totalCount: e.target.value })
                    }
                  />
                </Grid>
  
                <Grid item xs={4}>
                  <TextField
                    fullWidth
                    type="number"
                    label="가드 인원"
                    value={notice.guardCount}
                    onChange={(e) =>
                      setNotice({ ...notice, guardCount: e.target.value })
                    }
                  />
                </Grid>
  
                <Grid item xs={4}>
                  <TextField
                    fullWidth
                    type="number"
                    label="포워드 인원"
                    value={notice.forwardCount}
                    onChange={(e) =>
                      setNotice({ ...notice, forwardCount: e.target.value })
                    }
                  />
                </Grid>
  
                <Grid item xs={4}>
                  <TextField
                    fullWidth
                    type="number"
                    label="센터 인원"
                    value={notice.centerCount}
                    onChange={(e) =>
                      setNotice({ ...notice, centerCount: e.target.value })
                    }
                  />
                </Grid>
  
                <Grid item xs={12} sx={{ textAlign: "center", mt: 2 }}>
                  <Button
                    type="submit"
                    variant="contained"
                    size="large"
                    sx={{
                      backgroundColor: "#1976d2",
                      borderRadius: 8,
                      px: 6,
                      py: 1.5,
                      fontWeight: "bold",
                    }}
                  >
                    공지 등록하기
                  </Button>
                </Grid>
              </Grid>
            </form>
          </CardContent>
        </Card>
      </Box>
    );
}