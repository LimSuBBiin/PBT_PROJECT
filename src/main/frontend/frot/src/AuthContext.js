import React from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = React.createContext();

export const AuthProvider = ({ children }) => {
    const navigate = useNavigate();
    const [user, setUser] = React.useState(null);
    const token = localStorage.getItem('accessToken');
    const [memMstIdx, setMemMstIdx] = React.useState(null);
    const getAccessToken = () => localStorage.getItem('accessToken');
    const isTokenExpired = (token) => {
        if (!token) return true;
        try {
            const decoded = JSON.parse(atob(token.split(".")[1]));
            return decoded.exp < Math.floor(Date.now() / 1000); // 현재 시간과 비교

        } catch (e) {
            return true;
        }
    };





    const refreshToken = async () => {
        try{
            const response = await fetch("http://localhost:8081/auth/refresh", {
                method : "POST",
                credentials: "include",
            });

            if (response.ok) {
                const data = response.json();
                localStorage.setItem("accessToken", data.accessToken);
                console.log("accessToken 갱신 성공:", data.accessToken);
                return data.accessToken;
            } else {
                console.error("❌ Refresh Token 만료됨. 재로그인 필요");
                setUser(null);
                localStorage.removeItem("accessToken");
                return null;
            }
        
        }     catch (error) {
            console.error("❌ Access Token 갱신 중 오류 발생", error);
            return null;
        }
    }

    const fetchUser = async () => {
        try {
            let token = getAccessToken();
            
            if (isTokenExpired(token)) {
                console.log("⚠️ Access Token이 만료됨. Refresh 요청 진행...");
                token = await refreshToken();
                if (!token) return; // 토큰 갱신 실패 시 중단
            }

            const response = await fetch("http://localhost:8081/auth/me", {
                method: "GET",
                headers: { Authorization: `Bearer ${token}` },
                credentials: "include",
            });

            if (response.ok) {
                const userData = await response.json();
                setMemMstIdx(userData.memMstIdx);
                console.log("✅ 현재 idx값과 token값:", memMstIdx, token);
                setUser(userData);
                localStorage.setItem("memMstIdx", userData.memMstIdx);
            } else {
                setUser(null);
                setMemMstIdx(null);
                localStorage.removeItem("memMstIdx");
            }
        } catch (error) {
            setUser(null);
            setMemMstIdx(null);
            localStorage.removeItem("memMstIdx");
        }
    };

    console.log("현재 로그인한 유저 :", user);

      // memMstIdx 상태 변경 시 로그 출력 (확인용)
  React.useEffect(() => {
    console.log("✅ 현재 memMstIdx 값:", memMstIdx);
  }, [memMstIdx]);

      // 로그아웃 처리 함수
  const handleLogout = async () => {
    try{
    const response = await fetch(`http://localhost:8081/auth/logout`,{
      method: "POST",
      credentials : "include", 
    });

    if(response.ok) {
      localStorage.removeItem("token");
      localStorage.removeItem("memMstIdx");
      setUser(null);
      setMemMstIdx(null);
      navigate("/login");
    } else {
      console.log("로그아웃 실패", response.status);
    }

  
  } catch (error) {
    console.error("로그아웃 요청 중 오류 발생", error);
  }
  };


    React.useEffect(() => {
        const token = localStorage.getItem("accessToken");
        if(token){
        fetchUser();
        }
    }, []);

    return (
        <AuthContext.Provider value={{ user, fetchUser, handleLogout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => React.useContext(AuthContext);
