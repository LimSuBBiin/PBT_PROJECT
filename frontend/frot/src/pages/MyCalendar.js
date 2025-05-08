import { useEffect, useState } from "react";
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { StyledDate } from './MyCalendarWrapper';
import { useSelector } from "react-redux";
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
    Chip,
    Tooltip
  } from "@mui/material";
  import moment from "moment";
import SportsBasketballIcon from "@mui/icons-material/SportsBasketball";
import { MyCalendarWrapper, StyledCalendar } from './MyCalendarWrapper';
const CalendarComponent = () => {
  const [value, setValue] = useState(new Date());
  const [notices, setNotices] = useState([]);
  const teamIdx = useSelector((state) => state.team.teamIdx);
  const teamRequestIdx = useSelector((state) => state.teamMember.teamRequestIdx);
  const token = localStorage.getItem("accessToken");
  const memMstIdx = localStorage.getItem("memMstIdx");


  useEffect(() => {
    fetchMyReq(memMstIdx);
  }, [memMstIdx]);
  // useEffect(() => {
  //   if (teamIdx && teamRequestIdx) {
  //     fetchNoticeData(teamIdx, teamRequestIdx);
  //   }
  // }, [teamIdx, teamRequestIdx]);    

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







  return (
<Box sx={{ padding: 4, overflow: "auto", height: "100vh" }}>
  {/* <Box display="flex" alignItems="center" gap={1} mb={3}>
    <SportsBasketballIcon color="primary" sx={{ fontSize: 32 }} />
    <Typography variant="h5" fontWeight="bold" color="primary">
      내 경기 스케줄
    </Typography>
    

  </Box> */}
  <Box sx={{height: "90vh", width: "100%", display: "flex", justifyContent: "center", alignItems: "center" }}>
    <MyCalendarWrapper>
    <StyledCalendar
  onChange={setValue}
  value={value}
  calendarType="gregory"
  formatDay={(locale, value) => moment(value).format("D")}
  tileContent={({ date, view }) => {
    if (view !== 'month') return null; // 월(Month) 뷰에서만 표시

    const formattedDate = moment(date).format("YYYY-MM-DD");
    
    const matchedNotices = notices.filter(
      (n) => moment(n.noticeGameDate).format("YYYY-MM-DD") === formattedDate
    );

    if(matchedNotices.length === 0 ) return null;

    const tooltipContent = (
      <Box sx={{ p: 1, maxWidth: 250 }}>
        {matchedNotices.map((n, idx) => (
          <Box key={idx} sx={{ mb: 1 }}>

            <Typography variant="subtitle2" color="primary" fontWeight="bold">
            👨‍👩‍👧‍👦{n.teamNme}
            </Typography>
            <Typography variant="body2"> {n.noticeTitle}</Typography>
            <Typography variant="body2">🏟️ {n.noticeLocation}</Typography>
            <Typography variant="body2">🧍 매니저: {n.memNme}</Typography>
            <Typography variant="body2">
              신청한 포지션 : 
            <Chip
              label={`🏀 ${n.position}`}
              size="small"
              color="info"
              sx={{ mt: 0.5 }}
            />
            </Typography>

          </Box>
        ))}
      </Box>
    );


    

    return matchedNotices.length > 0 ? (
      
      <Tooltip title={tooltipContent} arrow placement="top" enterTouchDelay={0}>

      <Box
        sx={{
          mt: 1,
          p: 1,
          bgcolor: 'rgba(255, 255, 255, 0.8)',
          borderRadius: 1,
          border: "1px solidrgb(207, 230, 249)",
          boxShadow: 1,
          fontSize: "0.75rem",
        }}
      >
        {matchedNotices.map((n, idx) => (
          <Box key={idx} sx={{ mb: 1 }}>
            <Typography variant="subtitle2" color="primary" fontWeight="bold" noWrap>
              📌 {n.noticeTitle}
            </Typography>
            <Typography variant="body2" sx={{ color: "text.secondary" }}>
              🏟️ {n.noticeLocation}
            </Typography>
            <Typography variant="body2" sx={{ color: "text.secondary" }}>
              🧍 매니저 : {n.memNme}
            </Typography>
            <Chip
              label={`🏀 ${n.position}`}
              size="small"
              color="info"
              sx={{ mt: 0.5 }}
            />
          </Box>
        ))}
      </Box>

      </Tooltip>
        
      
    ) : null;
  }}
/>

{/* <StyledDate onClick={handleTodayClick}>오늘</StyledDate> */}
    </MyCalendarWrapper>
  </Box>

</Box>
  );
};

export default CalendarComponent;