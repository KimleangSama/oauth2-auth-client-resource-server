------------------------------------------------------- oauth2-authorization-code-client -----------------------------

insert into oauth2_client (client_id, client_id_issued_at, client_name, client_secret, client_secret_expires_at,
                           authentication_method, authorization_grant_type, scopes, redirect_uris, registered)
VALUES ( -- password
           'authorization-code-client-id', CURRENT_TIMESTAMP, 'authorization-code-client-name', '{noop}secret1', null,
           'client_secret_post', 'authorization_code,refresh_token', 'message.read,message.write',
           'http://localhost:8080/client/authorized', false);

insert into oauth2_client_setting (client_id, require_authorization_consent)
VALUES ( -- password
           'authorization-code-client-id', true);

insert into oauth2_client_token_setting (client_id, access_token_time, access_token_time_unit, refresh_token_time,
                                         refresh_token_time_unit)
VALUES ( -- password
           'authorization-code-client-id', 1, 'day', 4, 'day');


------------------------------------------------------ Client-Credentials-client -------------------------------------

insert into oauth2_client (client_id, client_id_issued_at, client_name, client_secret, client_secret_expires_at,
                           authentication_method, authorization_grant_type, scopes, redirect_uris, registered)
VALUES ( -- password
           'client-credentials-client-id', CURRENT_TIMESTAMP, 'client-credentials-client-name', '{noop}secret2', null,
           'client_secret_basic', 'client_credentials,refresh_token', 'message.read,message.write', null, false);


insert into oauth2_client_token_setting (client_id, access_token_time, access_token_time_unit, refresh_token_time,
                                         refresh_token_time_unit)
VALUES ( -- password
           'client-credentials-client-id', 1, 'hour', 4, 'hour');
