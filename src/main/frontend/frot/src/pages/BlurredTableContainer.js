import { styled } from '@mui/material/styles';
import { TableContainer, Paper } from '@mui/material';
import slamdunkImage from '../images/slamdunk4.jpg'

const BlurredTableContainer = styled(TableContainer)(({ theme }) => ({
  position: 'relative',
  overflow: 'hidden',
  '&::before': {
    content: '""',
    position: 'absolute',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    backgroundImage: `url(${slamdunkImage})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    filter: 'blur(2px)',
    opacity: 0.3,
    zIndex: 0,
  },
  '& .MuiTable-root': {
    position: 'relative',
    zIndex: 1,
  },
}));

export default BlurredTableContainer;