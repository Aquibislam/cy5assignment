import { Component } from 'react';
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import logo from './cy5.jpg';
import assignment from './assignment.png';

class Cy5Bar extends Component{

    render=()=>{
        return(
         <div>
            <div>

               <Navbar bg="dark" variant="dark">
                  <Container>
                 

                   <img
                    alt=""
                    src={logo}
                    width="140"
                    height="60"
                    className="d-inline-block align-top"
                   />{' '}
                    <img
                    alt=""
                    src={assignment}
                    width="150"
                    height="37"
                    className="d-inline-block align-top"
                   />
                  
                  {/* <h3  align="left" style={{ color: 'white' }}>Cy5-Project</h3>  */}
                  
                  
                   
          
                  </Container>
            </Navbar>
            </div>
         </div>

        );
    }

}
export default Cy5Bar;