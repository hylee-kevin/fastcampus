package main

import (
        "fmt"
        "os"
        "log"
        "net"
        "net/http"
)
func handler(w http.ResponseWriter, r *http.Request){
    name, err := os.Hostname()
    if err != nil {
         fmt.Printf("error: %v\n", err)
         return
    }
    fmt.Fprintln(w, "hostname: ", name)

    addr, err := net.LookupHost(name)
    if err != nil {
         fmt.Printf("error: %v\n", err)
         return
    }
    fmt.Fprintln(w, "IP: ", addr)

    for _, a := range addr {
        fmt.Println(a)
    }
}
func main() {
    fmt.Fprintln(os.Stdout, "Go!!! Go Application ......")
      http.HandleFunc("/",handler)
      log.Fatal(http.ListenAndServe(":9090",nil))
}


~devteam_go$ vi gostart.go
package main

import (
        "fmt"
        "os"
        "log"
        "net/http"
)
func handler(w http.ResponseWriter, r *http.Request){
    name, err := os.Hostname()
    if err != nil {
      panic(err)
    }

    fmt.Fprintln(w, "hostname:", name)
}
func main() {
    fmt.Fprintln(os.Stdout,"Starting Go App. Server......")
      http.HandleFunc("/",handler)
      log.Fatal(http.ListenAndServe(":8080",nil))
}
