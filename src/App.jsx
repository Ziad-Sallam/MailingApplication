import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import MailBox from "./Components/MailBox.jsx";
import './App.css'
import {useParams} from 'react-router-dom'
import SideBar from "./Components/SideBar.jsx";
import ListBox from "./Components/ListBox.jsx";
import MailView from "./Components/MailView.jsx";
import {Outlet} from 'react-router-dom'
import NavBar from "./Components/NavBar.jsx";


function App() {
    const params = useParams();
    console.log(params.user);


  return (
    <>
      <NavBar user={params.user}/>
      <div className={"main-container"}>
        <SideBar/>
        <Outlet/>
      </div>
    </>
  )
}

export default App
