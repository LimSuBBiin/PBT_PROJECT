import React from 'react';
import { createContext, useState, useEffect, useContext } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, BrowserRouter } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Drawer, List, ListItem, ListItemText, Box, Container, CssBaseline, Button, Divider } from '@mui/material';
import SignUp from './pages/SignUp';
import SignIn from './pages/login';
import MyPage from './pages/MyPage';
import EditMyPage from './pages/EditMyPage';
import CreateTeam from './pages/CreateTeam';
import MyTeamList from './pages/MyTeamList';
import FindTeamList from './pages/FindTeamList';
import Home from './pages/Home';
import { AuthProvider, useAuth } from './AuthContext';
import { Navigate } from 'react-router-dom'; 
import TeamManagement from './pages/TeamManagement';
import CreateNotice from './pages/CreateNotice';
import TeamSelectToggle from './pages/TeamSelectToggle';
import NoticeList from './pages/NoticeList';
import CalendarComponent from './pages/MyCalendar';
import { ThemeProvider } from 'styled-components';
// ✅ 로그인 여부에 따라 페이지 접근 제한
const ProtectedRoute = ({ children }) => {
  const { user } = useAuth();
  return user ? children : <Navigate to="/login" />;
};

const theme = {
  gray_1: "#333",
  red_1: "#ff0000",
  primary_2: "#1976d2",
  darkBlack: "#000000",
  yellow_2: "#ffeb3b",
  gray_5: "#f5f5f5",
};


const App = () => {
  return (
    <BrowserRouter>
    <AuthProvider>
 
        <CssBaseline />
        {/* <Header /> */}
        <Routes>
          <Route path="/signup" element={<SignUp />} />
          <Route path="/login" element={<SignIn />} />
          <Route path="/mypage" element={<ProtectedRoute><MyPage /></ProtectedRoute>} />
          <Route path="/home" element={<ProtectedRoute><Home /></ProtectedRoute>} />
          <Route path="/edit-my-page" element={<ProtectedRoute><EditMyPage /></ProtectedRoute>} />
          <Route path="/team/create" element={<ProtectedRoute><CreateTeam /></ProtectedRoute>} />
          <Route path="/team/myteam/list" element={<ProtectedRoute><MyTeamList /></ProtectedRoute>} />
          <Route path="/team/recommand/list" element={<ProtectedRoute><FindTeamList /></ProtectedRoute>} />
          <Route path="/team/execute/list" element={<ProtectedRoute><TeamManagement/></ProtectedRoute>} />
          <Route path="/team/notice/Create" element={<ProtectedRoute><CreateNotice/></ProtectedRoute>} />
          <Route path="/team/select/Myteams" element={<ProtectedRoute><TeamSelectToggle/></ProtectedRoute>} />
          <Route path="/notice/list" element={<ProtectedRoute><NoticeList/></ProtectedRoute>} />
          <Route path="/calendar" element={<CalendarComponent/>} />
          <Route path="/" element={<Navigate to="/home" />} />
        </Routes>
    </AuthProvider>
    </BrowserRouter>
  );
};

export default App;
