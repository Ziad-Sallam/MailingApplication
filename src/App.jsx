
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import MailBox from "./Components/MailBox.jsx";
import './App.css'
import {useParams} from 'react-router-dom'
import SideBar from "./Components/SideBar.jsx";
import ListBox from "./Components/ListBox.jsx";
import MailView from "./Components/MailView.jsx";
import {Outlet} from 'react-router-dom'


function App() {
    const params = useParams();
    console.log(params.user);

    const mails = [{
        sender : "ziad",
        receiver : "hello",
        subject : "hello world",
        body : "hello world",
        date : "22/1/2024",
        id : 5
    },
    {
        sender : "ziad",
        receiver : "hello",
        subject : "hello world",
        body : "hello world",
        date : "22/1/2024",
        id : 6
    }

    ]


  return (
    <>
      <div className={"main-container"}>
        <SideBar/>
        {/*<MailBox userEmail={"ziadsallam1234@gmail.com"}/>*/}
        {/*<ListBox mails={mails}/>*/}
        <Outlet/>
      </div>    </>
  )
}

export default App
