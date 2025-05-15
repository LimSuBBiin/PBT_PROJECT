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
  } from "@mui/material";
    
  export default function NoticeModal({ open, handleClose, notice, onRequestApply,  onRequestCancel, teamRequestIdx }) {
    

    
    if (!notice) return null;
    console.log("💡 onRequestApply props:", onRequestApply);
    console.log("값확인 teamRequestIdx: ",teamRequestIdx , "noticeGameIdx", notice.noticeGameIdx)
    const handleCancelClick = () => {
      if (!teamRequestIdx || !notice.noticeGameIdx) {
        console.log("값확인 ",teamRequestIdx , " ", notice.noticeGameIdx)
        console.warn("취소에 필요한 정보가 부족합니다.");
        return;
      }
      onRequestCancel(notice.noticeGameIdx, teamRequestIdx);
    };

    return (
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="md">
        <DialogTitle>{notice.noticeTitle}</DialogTitle>
        <DialogContent dividers>
          <Typography  sx={{ whiteSpace: "pre-line" }} gutterBottom>{notice.noticeDesc}</Typography>
          <Typography gutterBottom>📍 장소: {notice.noticeLocation}</Typography>
          <Typography gutterBottom>🗓 날짜: {new Date(notice.noticeGameDate).toLocaleString()}</Typography>
          <Typography gutterBottom>{`꒰⁎^﹏^⁎꒱ ${notice.memNme}`} 매니저가 진행합니다. ꒰⁎^﹏^⁎꒱</Typography>

          <Table>
            <TableHead>
              <TableRow>
                <TableCell>포지션</TableCell>
                <TableCell>신청 인원</TableCell>
                <TableCell>필요 인원</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              <TableRow>
                <TableCell>가드</TableCell>
                <TableCell>{notice.reqGuardCnt}</TableCell>
                <TableCell>{notice.guardCount}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>포워드</TableCell>
                <TableCell>{notice.reqForwardCnt} </TableCell>
                <TableCell>{notice.forwardCount}</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>센터</TableCell>
                <TableCell>{notice.reqCenterCnt}</TableCell>
                <TableCell>{notice.centerCount}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </DialogContent>
        
        <DialogActions>
        <Button onClick={onRequestApply} color="primary" variant="contained"> 신청하기 </Button>
        <Button onClick={handleCancelClick} color="error" variant="outlined">
          신청 취소
        </Button>
        </DialogActions>


        <DialogActions>
          <Button onClick={handleClose} color="primary">
            닫기
          </Button>
        </DialogActions>
      </Dialog>
    );
  }