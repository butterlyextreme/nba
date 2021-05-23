import org.springframework.cloud.contract.spec.Contract
Contract.make {
  description ""
  request{
    method DELETE()
    url("/game/43257/comment/43569ed-d170-41ee-81a0-8a257c1646ff") {
    }
    headers {
      header 'Content-Type': 'application/json'
    }
  }
  response {
    body("Generic Internal Error")
    status 500
  }
}
