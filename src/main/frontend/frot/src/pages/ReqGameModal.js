
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    Typography,
    Table,
    TableHead,
    TableRow,
    TableCell,
    TableBody,
    Box,
    TextField,
    ToggleButton,
    ToggleButtonGroup,
    Chip
  } from "@mui/material";
import { useState, useEffect } from "react";
import { useSelector } from "react-redux";


export default function ReqGameModal ({open, handleClose,  notice, onRequestApply, onRefresh}) {
  const teamRequestIdx = useSelector((state) => state.teamMember.teamRequestIdx);
  const noticeGameIdx = notice?.noticeGameIdx;
    const memMstIdx = localStorage.getItem("memMstIdx");
    const token = localStorage.getItem("accessToken");
    const [position, setPosition] = useState("main");
    const [customPosition, setCustomPosition] = useState("");
    const [userData, setUserData] = useState([]);
    const handlePositionChange = (event, newPosition) => {
        if (newPosition !== null) setPosition(newPosition);
         if (newPosition === "custom" && customPosition === "") {
      // custom 포지션으로 변경되면 customPosition 초기화
      setCustomPosition("PG"); // 기본 값으로 PG를 설정하거나, 다른 기본 값으로 설정할 수 있습니다.
    }
      };
    const [positionReq, setPositionReq] = useState("");
    

    
    const selectPosition =
    position === "main" 
        ? userData.mainPst
        : position === "sub"
        ? userData.subPst
        : position === "custom"
        ? customPosition
        : null;
    

        useEffect(() => {
          if (!notice || !userData) return;
        
          const positionStatus =
            position === "main"
              ? userData.mainPst
              : position === "sub"
              ? userData.subPst
              : position === "custom"
              ? customPosition
              : null;
        
          let msg = "해당 포지션 인원 상태는 원할합니다.";
        
          if ((positionStatus === "PG" || positionStatus === "SG") && notice.guardCount <= notice.reqGuardCnt) {
            msg = "해당 포지션은 포화상태입니다.";
          } else if ((positionStatus === "SF" || positionStatus === "PF") && notice.forwardCount <= notice.reqForwardCnt) {
            msg = "해당 포지션은 포화상태입니다.";
          } else if (positionStatus === "C" && notice.centerCount <= notice.reqCenterCnt) {
            msg = "해당 포지션은 포화상태입니다.";
          }
        
          setPositionReq(msg);
          console.log(msg);  // 상태가 업데이트되는지 확인
        }, [position, customPosition, userData, notice]);    
    
    

    const fetchReqData = async () => {
      if(!memMstIdx) {
        console.error("memMstIdx값이 존재하지 않습니다.")
        return;
      }



      if (notice.totalCount <= notice.reqGuardCnt + notice.reqForwardCnt + notice.reqCenterCnt) {
        alert("모집 인원이 가득찬 경기입니다.");
        return;
      }

      try{
        const response = await fetch(`http://localhost:8081/notice/detail/request?memMstIdx=${memMstIdx}&noticeGameIdx=${noticeGameIdx}`,
         {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
        );

        if(!response.ok) {
          throw new Error("Error fetching reqfoam data");
        }
        
        

        const userDatas = await response.json();
        setUserData(userDatas);


      } catch ( error) {
        console.log("Error fetching user and notice Data:", error);
      }
    }
    useEffect(() => {
      console.log("📦 ReqGameModal 렌더됨 - open:", open);
      console.log("notice:", notice);
      console.log("mem:", userData);
    }, [open]);

    useEffect(() => {
      if (open) {
        fetchReqData();
      }
    }, [open]);


    const handleJoinGame = async (event) => {
      
      const finalPosition =
    position === "main"
      ? userData.mainPst
      : position === "sub"
      ? userData.subPst
      : position === "custom"
      ? customPosition
      : null;  

      if (notice.totalCount <= notice.reqGuardCnt + notice.reqForwardCnt + notice.reqCenterCnt) {
        alert("모집 인원이 가득찬 경기입니다.");
        return;
      }

      if(!teamRequestIdx) {
        console.log("teamMemerIdx값이 존재하지 않습니다.");
        return;
      }
      
      if(!noticeGameIdx) {
        console.log("noticeGameIDx값이 존재하지 않습니다.");
        return;
      }

      try{
        const response = await fetch(`http://localhost:8081/notice/detail/request`, {
          method : "POST",
          headers: {
            "Content-type" : "application/json",
            Authorization : `Bearer ${token}`,
          },
          body: JSON.stringify({
            teamRequestIdx: teamRequestIdx,
            noticeGameIdx: noticeGameIdx,
            position: finalPosition
          }),
        });

        const data = await response.json();
        

        if (!response.ok) {
          alert(data.message || "알 수가 없는 오류가 발생했습니다.")
          // 에러 상태일 때, 서버가 준 메시지를 alert로 띄우기
          return; // 에러났으면 여기서 return
        }
        console.log("신청 완료", data);
        alert("신청 완료!");

      } catch (error) {
        console.log("가입 신청 중 오류 발생", error);
      }

    } ;


    if (!notice) {
      return null; // notice가 없으면 modal을 렌더링하지 않음
    }
    return(
        <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
             <DialogTitle>{`${notice.noticeTitle}의 경기 신청`}</DialogTitle>
             <DialogContent dividers>
             <Typography gutterBottom>신청자: {userData.memNme}</Typography>
             <Box my={2}>
          <Typography gutterBottom>포지션 선택</Typography>
          <Typography 
               variant="body1"  sx={{ fontWeight: 'bold', mt: 1 }}
              >
                선택된 포지션: <strong style={{marginRight: '80px'}}>{selectPosition}</strong>          <strong style={{color: positionReq.includes("포화") ? 'red' : 'green'}}>{positionReq}</strong></Typography>
          <ToggleButtonGroup
            value={position}
            exclusive
            onChange={handlePositionChange}
            fullWidth
          >
            <ToggleButton value="main">메인 포지션</ToggleButton>
            <ToggleButton value="sub">서브 포지션</ToggleButton>
            <ToggleButton value="custom">직접 선택</ToggleButton>


            </ToggleButtonGroup>
            {position === "custom" ? (
            <ToggleButtonGroup
             exclusive
              value={customPosition}
              onChange={(e) => setCustomPosition(e.target.value)}
              fullWidth
            >
        <ToggleButton value="PG">PG</ToggleButton>
      <ToggleButton value="SG">SG</ToggleButton>
      <ToggleButton value="SF">SF</ToggleButton>
      <ToggleButton value="PF">PF</ToggleButton>
      <ToggleButton value="C">C</ToggleButton>
              </ToggleButtonGroup>
          ) : (
            // 나중에 원할 보통 불가 표시 예정
            <Box mt={2} display="flex" alignItems="center">
           
              <Chip
              label={selectPosition || "선택 안 됨"}
                size="small"
                sx={{ ml: 2 }}
              />
            </Box>
          )}
            </Box>

            <TextField
          fullWidth
          label="키(cm)"
          value={userData.memHeight}
          margin="normal"
          InputProps={{ readOnly: true }}
        />
        <TextField
          fullWidth
          label="구력(년)"
          value={userData.memHist}
          margin="normal"
          InputProps={{ readOnly: true }}
        />

<Typography variant="body2" color="textSecondary" mt={2}>
          * 개인 농구화, 준비 물품들 필수입니다. *
        </Typography>
                </DialogContent>



                <DialogActions>
        <Button onClick={handleClose} color="inherit">
          취소
        </Button>
        <Button variant="contained" color="primary" onClick={() => { handleJoinGame(teamRequestIdx, noticeGameIdx);  onRefresh(); handleClose();}}>
          신청 완료하기
        </Button>
      </DialogActions>       
            </Dialog>
    )
    
}
