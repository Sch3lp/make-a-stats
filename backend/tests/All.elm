module All exposing (..)

import Expect exposing (Expectation)
import Test exposing (..)
import Json.Decode exposing (decodeString)
import PUBG.Player exposing (..)
import PUBG.Common exposing (..)
import Fixtures.Player exposing (..)


suite : Test
suite =
    describe "Player"
        [ test "can decode a Player from a PUBG response" <|
            \_ ->
                decodeString (wrap playerDecoder) playerResponse
                    |> Debug.log "actualResponse"
                    |> Result.toMaybe
                    |> Maybe.andThen unwrapFirst
                    |> Maybe.map .id
                    |> Expect.equal (Just "account.2b95c68272fd467db565f5134277993b")
        ]
