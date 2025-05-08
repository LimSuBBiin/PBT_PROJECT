import React, { useEffect } from 'react';
import {Card, Box, Button, Typography, Divider, AppBar, Toolbar, Container, Tab, Tabs, MenuItem, IconButton, Drawer } from '@mui/material';
import { Link } from 'react-router-dom';
import MyPage from './MyPage';
import CloseRoundedIcon from '@mui/icons-material/CloseRounded';
import MyTeamsList from './MyTeamList';
import FindTeamList from './FindTeamList';
import { styled, alpha } from '@mui/material/styles';
import ColorModeIconDropdown from './ColorModelDropDown';
import MenuIcon from '@mui/icons-material/Menu';
import TeamManagement from './TeamManagement';
import CreateTeam from './CreateTeam';
import CreateNotice from './CreateNotice';
import EditMyPage from './EditMyPage';
import TeamSelectToggle from './TeamSelectToggle';
import { useDispatch } from 'react-redux';
import { useSelector } from 'react-redux';
import { setTeamIdx } from '../store/teamSlice';
import NoticeList from './NoticeList';
import { setTeamRequestIdx } from '../store/teamMemberSlice';
import '@fontsource/bebas-neue'; 
import CalendarComponent from './MyCalendar';
import slamdunkImage from '../images/slamdunk1.jpg';
const StyledToolbar = styled(AppBar)(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  flexShrink: 0,
  borderRadius: `calc(${theme.shape.borderRadius}px + 8px)`,
  backdropFilter: 'blur(24px)',
  border: '1px solid',
  borderColor: (theme.vars || theme).palette.divider,
  backgroundColor: theme.vars
    ? `rgba(${theme.vars.palette.background.defaultChannel} / 0.4)`
    : alpha(theme.palette.background.default, 0.4),
  boxShadow: (theme.vars || theme).shadows[1],
  padding: '4px 8px',
}));

const Home = () => {
  const [activeTab, setActiveTab] = React.useState(0);
  const [open, setOpen] = React.useState(false);
  const memMstIdx = Number(localStorage.getItem("memMstIdx"));
  const token = localStorage.getItem("accessToken");
  console.log("memMstIdx값 확인:",memMstIdx);
  const [teamData, setTeamData] = React.useState([]);
  const [isLoading, setIsLoading] = React.useState(true);
  const [selectedTeamIdx, setSelectedTeamIdx] = React.useState("");
  const dispatch = useDispatch();
  const teamIdx = useSelector((state) => state.team.teamIdx);
  const teamRequestIdx = useSelector((state) => state.teamMember.teamRequestIdx);
  console.log("teamIdx값 확인:",teamIdx);
  const toggleDrawer = (newOpen) => () => {
    setOpen(newOpen);
  };

 
    const fetchTeams = async () => {
      try{
      const response = await fetch(`http://localhost:8081/team/select/myTeam?memMstIdx=${memMstIdx}`,{
        method : "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
      );

      if(!response.ok) {
        throw new Error(`HTTP 오류 발생: ${response.status}`);
      }

      const data = await response.json();
      setTeamData(data);
      if (data.length > 0) {
        setSelectedTeamIdx(data[0].teamIdx);
        dispatch(setTeamIdx(data[0].teamIdx));
      }
    } catch (error) {
      console.error(error);
    }finally {
      setIsLoading(false); // 로딩 끝!
    }


    }


    

 
// 처음 로딩 시 실행
useEffect(() => {
  fetchTeams();
}, [memMstIdx, token, dispatch]);


  useEffect(() => {
    const fetchteamMember = async () => {
      try{
      const response = await fetch(`http://localhost:8081/notice/getTeamRequestIdx?teamIdx=${teamIdx}&memMstIdx=${memMstIdx}`, {
        method : "GET",
        headers : { Authorization : `Bearer ${token}`},
        credentials: "include",
      });

      if(!response.ok) {
        throw new Error("Error fetching teamRequestIdx: " + response.statusText);

      }

      const data = await response.json();
      dispatch(setTeamRequestIdx(data));
      console.log("팀 멤버 idx가져오기 성공:",data);
    } catch (errer) {
      console.log("Error fetching teamRequestIdx:", errer);
    }
  }
  if(memMstIdx&&teamIdx) {
    fetchteamMember();
  }
  },[memMstIdx, teamIdx, token, dispatch]);

  useEffect(() => {
    if (teamIdx) {
      console.log("변경된 teamIdx 확인:", teamIdx);
      console.log("토큰 확인:", token);
    }
  }, [teamIdx]);

  const handleTabChange = (event, newValue) => {
    setActiveTab(newValue);
  };

  return (
    <Box sx={{ backgroundColor: "#F9F1E4", maxWidth: "none"}}>
      {/* 첫 번째 AppBar */}
      <StyledToolbar position="fixed" enableColorOnDark sx={{ boxShadow: 0, bgcolor: 'transparent', mt: 'calc(var(--template-frame-height, 0px) + 2px)' }}>
        <Container maxWidth="lg">
          <Box sx={{ flexGrow: 1, display: 'flex', alignItems: 'center', px: 0 }}>
            <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
              <Button variant="text" color="info" size="small" onClick={() => setActiveTab(0)}>
                마이 페이지
              </Button>
              <Button variant="text" color="info" size="small" onClick={() => setActiveTab(1)}>
                내 팀 목록
              </Button>
              <Button variant="text" color="info" size="small" onClick={() => setActiveTab(2)}>
                팀 찾기
              </Button>
              <Button variant="text" color="info" size="small" onClick={() => setActiveTab(3)}>
                팀 관리
              </Button>
              <Button variant="text" color="info" size="small" onClick={() => setActiveTab(4)}>
                팀 생성
              </Button>

              <Button variant="text" color="info" size="small" onClick={() => setActiveTab(5)}>
                경기 참가 글 생성
              </Button>

              <Button variant="text" color="info" size="small" onClick={() => setActiveTab(6)}>
                내 팀 글목록
              </Button>

              <Button variant="text" color="info" size="small" onClick={() => setActiveTab(7)}>
                내 경기 스케줄
              </Button>

            </Box>
          </Box>
          <Box sx={{ display: { xs: 'none', md: 'flex' }, gap: 1, alignItems: 'center' }}>
            <Button color="primary" variant="text" size="small">
              Sign in
            </Button>
            <Button color="primary" variant="contained" size="small">
              Sign up
            </Button>

            <Box sx={{ position: 'absolute', top: 0, right: 1, p: 2 }}>
      <Typography variant="body1">팀 선택:</Typography>
      <Box width={200}>
      
        <TeamSelectToggle
          teamList={teamData}
          selectedTeamIdx={selectedTeamIdx}
          onChange={(newTeamIdx) => {
            setSelectedTeamIdx(newTeamIdx);
            dispatch(setTeamIdx(newTeamIdx));
          }}
          isLoading={isLoading}
        />
      </Box>
    </Box>
            <ColorModeIconDropdown />
          </Box>
          <Box sx={{ display: { xs: 'flex', md: 'none' }, gap: 1 }}>
            <ColorModeIconDropdown size="medium" />
            <IconButton aria-label="Menu button" onClick={toggleDrawer(true)}>
              <MenuIcon />
            </IconButton>
            <Drawer anchor="top" open={open} onClose={toggleDrawer(false)}>
              {/* <Box sx={{ p: 2, backgroundColor: 'background.default' }}>
                <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
                  <IconButton onClick={toggleDrawer(false)}>
                    <CloseRoundedIcon />
                  </IconButton>
                </Box>
                <MenuItem>
                  <Button variant="text" color="info" size="small" onClick={() => setActiveTab(0)}>
                    마이 페이지
                  </Button>
                </MenuItem>
                <MenuItem>
                  <Button variant="text" color="info" size="small" onClick={() => setActiveTab(1)}>
                    내 팀 목록
                  </Button>
                </MenuItem>
                <MenuItem>
                  <Button variant="text" color="info" size="small" onClick={() => setActiveTab(2)}>
                    팀 찾기
                  </Button>
                </MenuItem>
                <MenuItem>
                <Button variant="text" color="info" size="small" onClick={() => setActiveTab(3)}>
                  팀 관리
                  </Button>
                </MenuItem>
                <MenuItem>
                <Button variant="text" color="info" size="small" onClick={() => setActiveTab(4)}>
                  팀 생성
                  </Button>
                </MenuItem>
                <MenuItem>
                  <Button variant="text" color="info" size="small" onClick={() => setActiveTab(5)}>
                    경기 참가 글 생성
                  </Button>
                </MenuItem>

                <MenuItem>
                  <Button variant="text" color="info" size="small" onClick={() => setActiveTab(6)}>
                   내 팀 글목록
                  </Button>
                </MenuItem>

                <MenuItem>
                  <Button variant="text" color="info" size="small" onClick={() => setActiveTab(7)}>
                   내 경기 스케줄
                  </Button>
                </MenuItem>


                <Divider sx={{ my: 3 }} />
                <MenuItem>
                  <Button color="primary" variant="contained" fullWidth>
                    Sign up
                  </Button>
                </MenuItem>
                <MenuItem>
                  <Button color="primary" variant="outlined" fullWidth>
                    Sign in
                  </Button>
                </MenuItem>
              </Box> */}
            </Drawer>
          </Box>
        </Container>
      </StyledToolbar>
      <Box sx={{
      backgroundImage: `url(${slamdunkImage})`,
      backgroundSize: 'cover',
      backgroundRepeat: 'no-repeat',
      backgroundPosition: 'center',
      borderRadius: '12px',
      padding: '50px 20px', // 상하 여백을 넉넉하게 줌
      minHeight: '300px',   // 기본 높이 지정
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      width: '100vw', 
      maxWidth: '100%',  
  }}>
      <Typography
    variant="h2"
    sx={{
      fontFamily: 'Bebas Neue, sans-serif',
      fontSize: '9rem',
      letterSpacing: 2,
      color: '#FF6F00', // 농구 오렌지
      textShadow: '2px 2px 4px rgba(0,0,0,0.3)',
      padding: '15px',
      // backgroundImage: `url(${slamdunkImage})`,
      // backgroundSize: 'cover',
      // backgroundRepeat: 'no-repeat',
      // backgroundPosition: 'center',
      // borderRadius: '12px',
      display: 'inline-block'
    }}
  >
    BASKET ZONE
  </Typography>
        <Typography variant="h3" paragraph    
          sx={{
              fontFamily: 'Oswald, sans-serif',
              color: '#FF6F00',
              letterSpacing: 1.5,
              px: 2,
              py: 1,
              borderRadius: '8px',
              textAlign: 'center',
              maxWidth: '600px',
              animation: 'shake 2s infinite ease-in-out',
              '@keyframes shake': {
                '0%': { transform: 'translateX(0)' },
                '25%': { transform: 'translateX(-5px)' },
                '50%': { transform: 'translateX(5px)' },
                '75%': { transform: 'translateX(-5px)' },
                '100%': { transform: 'translateX(0)' },
              },
  }}>
          Show off your burning passion for basketball
        </Typography>
    </Box>
      {/* Main Content */}
      <Container sx={{ textAlign: 'center', marginTop: 10, width: '100%',maxWidth: 'none' }}>
        {/* 탭에 따라 표시할 내용 */}
        <Box sx={{ mt: 4  ,  
        width: "130%",
        maxWidth: "130vw",
         overflowX: "auto",
         transform: "translateX(-10%)"
         }}>
          {activeTab === 0 && <MyPage setActiveTab={setActiveTab}/>}
          {activeTab === 1 && <MyTeamsList />}
          {activeTab === 2 && <FindTeamList />}
          {activeTab === 3 && <TeamManagement/>}
          {activeTab === 4 && <CreateTeam setActiveTab={setActiveTab} refetchTeams = {fetchTeams}/>}
          {activeTab ===5 && <CreateNotice setActiveTab={setActiveTab} />}
          {activeTab === 6 && <NoticeList/>}
          {activeTab === 7 && <CalendarComponent/>}
          {activeTab === 8 && <EditMyPage setActiveTab={setActiveTab}/>}
        </Box>

        <Divider sx={{ mt: 5 }} />
      </Container>
    </Box>
  );
};

export default Home;
