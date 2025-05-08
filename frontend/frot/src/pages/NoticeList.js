import { useEffect, useState } from "react";
import {
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Card,
  Button,
  CardContent,
  TableContainer,
  Paper,
  Typography,
  Box,
} from "@mui/material";
import SportsBasketballIcon from "@mui/icons-material/SportsBasketball";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../AuthContext"; // 본인 프로젝트에 맞게 import 경로 조정
import NoticeModal from "./NoticeModal";
import ReqGameModal from './ReqGameModal';
import { format } from "date-fns";
import { ko } from "date-fns/locale"; // 한국어 로케일
export default function NoticeList() {
  const [notices, setNotices] = useState([]);
  const [selectNotice, setSelectNotice] = useState();
  const [openModal, setOpenModal] = useState(false);
  const navigate = useNavigate();
  const [openReqModal, setOpenReqModal] = useState(false);
  const { user } = useAuth();
  const teamIdx = useSelector((state) => state.team.teamIdx);
  const teamRequestIdx = useSelector((state) => state.teamMember.teamRequestIdx);
  const token = localStorage.getItem("accessToken");
  const memMstIdx = localStorage.getItem("memMstIdx");
  const [refresh,setRefresh] = useState();
  

  console.log("반응확인++(NoticeList)", token);
  console.log("현재 teamIDx확인(NoticeList):",teamIdx);
  console.log("현재 teamRequestIdx확인(NoticeList):",teamRequestIdx);
  
    // `handleOpenReqModal` 함수 정의
    const handleOpenReqModal = () => {
      setOpenReqModal(true); // ReqGameModal을 열기 위해 상태 변경
    };
  
      // `handleCloseReqModal` 함수 정의
  const handleCloseReqModal = () => {
    setOpenReqModal(false); // ReqGameModal을 닫기 위해 상태 변경
  };

  useEffect(() => {
    if (teamIdx && teamRequestIdx) {
      fetchNoticeData(teamIdx, teamRequestIdx);
    }
  }, [teamIdx, teamRequestIdx, refresh]);




  //전체 리스트 
  const fetchNoticeData = async (teamIdx, teamRequestIdx) => {
    if (!teamIdx || !teamRequestIdx) {
      console.log("각각의 idx값을 확인하시오.");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8081/notice/list?teamIdx=${teamIdx}&teamRequestIdx=${teamRequestIdx}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Error fetching notice");
      }

      const noticeData = await response.json();
      setNotices(noticeData);
    } catch (error) {
      console.error("Error fetching team data:", error);
    }
  };


  //내가 쓴글 
  const fetchMyNoticeData = async (teamIdx,teamRequestIdx,memMstIdx) => {
    if (!teamIdx || !teamRequestIdx || !memMstIdx) {
      console.log("각각의 idx값을 확인하시오.");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8081/notice/mylist?teamIdx=${teamIdx}&teamRequestIdx=${teamRequestIdx}&memMstIdx=${memMstIdx}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Error fetching notice");
      }

      const noticeData = await response.json();
      setNotices(noticeData);
    } catch (error) {
      console.error("Error fetching team data:", error);
    }

  }

  // 내가 신청한 경기
  const fetchMyReq = async (memMstIdx) => {
    if(!memMstIdx) {
      console.log("memMstIdx 값 확인");
      return;
    }

    try{
      const res = await fetch(`http://localhost:8081/notice/myReq?memMstIdx=${memMstIdx}`,{
        method : "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
      );
      if (!res.ok) {
        throw new Error("Error fetching notice");
      }

      const noticeData = await res.json();
      setNotices(noticeData);

    } catch (error) {
      console.error("Error fetching team data:", error);
    }
  }


    //noticeDetail
  const handleRowClick = async (noticeGameIdx) => {
 
    try{
      const response = await fetch(`http://localhost:8081/notice/detail?noticeGameIdx=${noticeGameIdx}`,
        {
        method : "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        }
      );

      if (!response.ok) {
        throw new Error("Error fetching team data");
      }

      const data =  await response.json();
      setSelectNotice(data);
      if(data.totalCount <= data.reqGuardCnt + data.reqForwardCnt + data.reqCenterCnt){
        alert("모집 인원이 가득찬 경기입니다.");
      }
      setOpenModal(true);

    } catch (error) {
      console.error("Error fetching team Data", error);
    }
  };


  //모달 취소 메소드 
  const handleRequestCancel = async (noticeGameIdx, teamRequestIdx) => {
    try{
      await fetch(`http://localhost:8081/notice/request/cancel?noticeGameIdx=${noticeGameIdx}&teamRequestIdx=${teamRequestIdx}`,{
        method : "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      alert("신청 취소 되었습니다.");
    } catch (error) {
      console.error("취소실패", error);
    }
  }


  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectNotice(null);
  };


  const handleRefresh = () => {
    setRefresh(prev => !prev); //강제로 recall
  }

  return (
    <Box sx={{ padding: 0, overflow: "auto" }}>
      <Box display="flex" alignItems="center" gap={1} mb={3}>
        <SportsBasketballIcon color="#E53933" sx={{ fontSize: 32 }} />
        <Typography variant="h5" fontWeight="bold" color="#E53935" sx={{fontSize: 40}}>
          WELCOME YOUR JOINING
        </Typography>
        <SportsBasketballIcon color="#E53933" sx={{ fontSize: 32 }} />
      </Box>
      <Box>
        <Button onClick={()=> fetchMyNoticeData(teamIdx,teamRequestIdx,memMstIdx)}>
          내가 쓴 글 보기 
        </Button>
        <Button onClick={()=> fetchNoticeData(teamIdx,teamRequestIdx)}>
          우리 팀 전체 글 보기
        </Button>

        <Button onClick={() => fetchMyReq(memMstIdx)}>
          내가 신청한 경기 보기
        </Button>
      </Box>
      <TableContainer
        component={Paper}
        sx={{
          maxWidth: 2000,
          width: "max-content",
          margin: "auto",
          boxShadow: 6,
          border: "2px solid #E53935",
          borderRadius: 2,
        }}
      >
        <Table>
          <TableHead>
            <TableRow sx={{ backgroundColor: "#E53935" }}>
              <TableCell sx={{ fontWeight: "bold", color: "white" }}></TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "white" }}>작성자(역할)</TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "white" }}>글제목</TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "white" }}>경기 위치</TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "white" }}>경기 날짜(시간)</TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "white" }}>모집 현황</TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "#fdd835" }}>가드</TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "#66bb6a" }}>포워드</TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "#42a5f5" }}>센터</TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "white" }}>상태</TableCell>
              <TableCell sx={{ fontWeight: "bold", color: "white" }}>조회수</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
  {notices.map((notice) => {
    const now = new Date();
    const gameDate = new Date(notice.noticeGameDate); // ISO 형식이면 이걸로 충분
    const isPast = gameDate < now;
    const applyStatus =
    notice.gameRequestStatus === "신청"
      ? "신청완료"
      : "미신청"; // 나머지는 모두 "미신청" 처리

    return (
      <TableRow
        key={notice.noticeGameIdx}
        onClick={() => handleRowClick(notice.noticeGameIdx)}
        hover
        sx={{ cursor: "pointer" }}
      >
        <TableCell>●</TableCell>
        <TableCell>{`${notice.memNme} (${notice.roleType})`}</TableCell>
        <TableCell sx={{ textDecoration: isPast ? "line-through" : "none" }}>
  <Typography
    sx={{
      display: "inline-block",
      paddingX: 1,  // px-3
      paddingY: 0.5,  // py-1
      borderRadius: "9999px",  // rounded-full
      fontSize: "0.875rem",  // text-sm
      fontWeight: "600",  // font-semibold
      color: isPast ? "gray" : "text.secondary",  // title 색상
    }}
  >
    {notice.noticeTitle}
  </Typography>
  <Typography
    sx={{
      display: "inline-block",
      paddingX: 1,  // px-3
      paddingY: 0.5,  // py-1
      borderRadius: "9999px",  // rounded-full
      fontSize: "0.875rem",  // text-sm
      fontWeight: "600",  // font-semibold
      color: applyStatus === "신청완료" ? "#1976d2" : "gray",  // applyStatus 색상
      marginLeft: 1, // 두 번째 텍스트 간격
    }}
  >
    [{applyStatus}]
  </Typography>
</TableCell>
        <TableCell>{notice.noticeLocation}</TableCell>
        <TableCell
          sx={{
            textDecoration: isPast ? "line-through" : "none",
            color: isPast ? "gray" : "inherit",
          }}
        >
        {format(new Date(notice.noticeGameDate), "yy년 M월 d일 a h시", { locale: ko })}
        </TableCell>
        <TableCell>
          {notice.reqGuardCnt + notice.reqForwardCnt + notice.reqCenterCnt} /{" "}
          {notice.totalCount}
        </TableCell>
        <TableCell sx={{ color: "#fbc02d" }}>
          {notice.reqGuardCnt} / {notice.guardCount}
        </TableCell>
        <TableCell sx={{ color: "#43a047" }}>
          {notice.reqForwardCnt} / {notice.forwardCount}
        </TableCell>
        <TableCell sx={{ color: "#1e88e5" }}>
          {notice.reqCenterCnt} / {notice.centerCount}
        </TableCell>

        <TableCell>
          {notice.gameStatus}
          </TableCell>  

         <TableCell>
          {notice.hit}
          </TableCell> 

      </TableRow>
    );
  })}
</TableBody>
        </Table>
      </TableContainer>

       {/* 모달 컴포넌트 */}
       <NoticeModal open={openModal} handleClose={handleCloseModal} notice={selectNotice}  onRequestApply={handleOpenReqModal} onRequestCancel={handleRequestCancel}
  teamRequestIdx={teamRequestIdx} />
             {/* ReqGameModal 컴포넌트 */}
      <ReqGameModal open={openReqModal} handleClose={handleCloseReqModal} notice={selectNotice} onRefresh = {handleRefresh}/>     
    </Box>
  );
}
