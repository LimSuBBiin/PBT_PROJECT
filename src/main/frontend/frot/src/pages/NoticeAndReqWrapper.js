import { useState } from "react";
import NoticeModal from "./NoticeModal";
import ReqGameModal from "./ReqGameModal"
export default function NoticeAndReqWrapper({ notice }) {
    const [noticeOpen, setNoticeOpen] = useState(false);
    const [reqOpen, setReqOpen] = useState(false);
    console.log("모달 열림 여부:", noticeOpen);
    console.log("넘어온 notice:", notice);
    console.log("넘어온 mem:", mem);
    const handleOpenReqModal = () => {
        setNoticeOpen(false);
        setReqOpen(true);
      };
      
      const handleCloseAll = () => {
        setNoticeOpen(false);
        setReqOpen(false);
      };

      const handleCloseReqModal = () => setReqOpen(false);
      return (
        <>
          <Button onClick={() => setNoticeOpen(true)}>공지 보기</Button>
    
          <NoticeModal
            open={noticeOpen}
            handleClose={() => setNoticeOpen(false)}
            notice={notice}
            onRequestApply={handleOpenReqModal} // 👈 신청하기 버튼 클릭 시
          />
    
          <ReqGameModal
            open={reqOpen}
            handleClose={handleCloseAll}
            notice={notice}
          />
        </>
      );
    }